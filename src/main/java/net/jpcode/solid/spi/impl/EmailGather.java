/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.apache.commons.io.IOUtils;

import com.sun.mail.pop3.POP3Folder;

import lombok.extern.slf4j.Slf4j;
import net.jpcode.solid.ContentService;
import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.ContentFilter;
import net.jpcode.solid.spi.ContentGather;

/**
 * @author: billy zhang
 */
@Slf4j
public class EmailGather implements ContentGather {
	
	private String server;
	private int port;
	private String username;
	private String password;

	private ContentService service;
	private List<ContentFilter> filters = new ArrayList<ContentFilter>();
	
	@Override
	public void setContentService(ContentService service) {
		this.service = service;
	}
	
	@Override
	public void init(Map<String, Object> data) {
		this.server = (String)data.get("server");
		this.port = (Integer)data.get("port");
		this.username = (String)data.get("username");
		this.password = (String)data.get("password");
		
		if (data.containsKey("filters")) {
			Map<String, Object> filterMap = (Map<String, Object>)data.get("filters");
			for (String name : filterMap.keySet()) {
				ContentFilter filter = service.createContentFilter(name);
				filter.init((Map<String, Object>)filterMap.get(name));
				filters.add(filter);
			}
		}
	}

	@Override
	public List<Content> gathe() {

        List<Content> list = new ArrayList<>();
        
		 // 准备连接服务器的会话信息 
        Properties props = new Properties(); 
        //props.setProperty("mail.store.protocol", "pop3");       // 协议 
        //props.setProperty("mail.pop3.port", "" + port);             // 端口 
        //props.setProperty("mail.pop3.host", server);    // pop3服务器 
        props.setProperty("mail.smtp.auth", "true");
         
        try {
	        // 创建Session实例对象 
	        Session session = Session.getDefaultInstance(props,null);
	        URLName urlname = new URLName("pop3",server,110,null,username, password);
	        Store store = session.getStore(urlname); 
	        store.connect(); 
	         
	        // 获得收件箱 
	        POP3Folder inbox = (POP3Folder)store.getFolder("INBOX"); 
	        /* Folder.READ_ONLY：只读权限
	         * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
	         */ 
	        inbox.open(Folder.READ_WRITE); //打开收件箱 
	        // 获得收件箱中的邮件总数 
	        log.info(String.format("接收:%s, 邮件总数:%d", username, inbox.getMessageCount())); 

	        // 得到收件箱中的所有邮件,并解析 
	        Message[] messages = inbox.getMessages(); 
	        
	        for (Message msg : messages) {
	        	try {
	        		String uid = inbox.getUID(msg);
	        		
	        		MessageContent content = new MessageContent(uid);
	        		content.parse(msg);
        			if (!acceptContent(content)) {
        				continue;
        			}
        			
        			while (true) {
		        		// 数据库查重
		        		if (service.selectContentCountByUID(uid) > 0) {
		        			break;
		        		}
		        		
			        	List<Attachment> attlist = new ArrayList<>();
			        	getAttachment(msg, attlist);
			        	if (attlist.size() > 0) {
			        		for (Attachment att : attlist) {
			        			AttachmentContent attContent = new AttachmentContent(uid, att.getFilename(), att.getData());
			        			if (acceptContent(attContent)) {
			        				list.add(attContent);
			        			}
			        		}
			        	}
			        	
			        	break;
        			}
        			
        			msg.setFlag(Flags.Flag.DELETED, true);
        			log.info("邮件删除标记：" + msg.getSubject());
        			
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        		log.error(ex.getMessage(), ex);
	        	}
	        }
	        
	        //释放资源 
	        inbox.close(true); 
	        store.close(); 
        } catch (Exception ex) {
        	ex.printStackTrace();
    		log.error(ex.getMessage(), ex);
        }
        
		return list;
	}

	private boolean acceptContent(Content content) {
		for (ContentFilter filter : filters) {
			if (!filter.accept(content)) {
				return false;
			}
		}
		
		return true;
	}
	
	 /**
     * 判断邮件中是否包含附件
     * @param msg 邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */ 
    private void getAttachment(Part part, List<Attachment> list) throws MessagingException, IOException { 
    	
    	if (part.isMimeType("multipart/*")) { 
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件 
            //复杂体邮件包含多个邮件体 
            int partCount = multipart.getCount(); 
            for (int i = 0; i < partCount; i++) { 
                //获得复杂体邮件中其中一个邮件体 
                BodyPart bodyPart = multipart.getBodyPart(i); 
                //某一个邮件体也有可能是由多个邮件体组成的复杂体 
                String disp = bodyPart.getDisposition(); 
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) { 
                	list.add(getAttachment(bodyPart));
                } else if (bodyPart.isMimeType("multipart/*")) { 
                    getAttachment(bodyPart, list); 
                } else { 
                    String contentType = bodyPart.getContentType(); 
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) { 
                    	list.add(getAttachment(bodyPart));
                    } 
                } 
            } 
        } else if (part.isMimeType("message/rfc822")) { 
            getAttachment((Part) part.getContent(), list); 
        } 
    	
    } 
    
    private Attachment getAttachment(BodyPart part) throws IOException, MessagingException {
    	Attachment att = new Attachment();
    	att.setFilename( MessageContent.decodeText(part.getFileName()) );
    	
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    	IOUtils.copy(part.getInputStream(), outStream);
    	att.setData(outStream.toByteArray());
    	
    	return att;
    }
    
	
}
