/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import net.jpcode.solid.model.Content;

/**
 * @author: billy zhang
 */
public class MessageContent implements Content {

	private final String uid;
	private String from;
	private String subject;
	private String content;
	
	@Override
	public String getUid() {
		return uid;
	}
	
	@Override
	public String getName() {
		return subject + ".htm"; 
	}
	
	@Override
	public String getFormat() {
		return "html";  // TODO:???
	}
	
	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public byte[] getData() {
		return null;
	}

	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public MessageContent(String uid) {
		this.uid = uid;
	}
	
	 public void parse(Message message) throws MessagingException, IOException { 
        
        // 解析邮件 
        MimeMessage msg = (MimeMessage) message; 
 
		subject = getSubject(msg);
        from = getFrom(msg); 
//	        to = getReceiveAddress(msg, null); 
//	        sentDate = getSentDate(msg, null); 
        
        StringBuffer buffer = new StringBuffer(30); 
        getContent(msg, buffer); 
        content = buffer.toString();
        
        //System.out.println("邮件主题：" + subject);
        //System.out.println("邮件大小：" + msg.getSize() / 1024 + "kb"); 
        //System.out.println("正文：" + text);
   
    } 

	/**
	 * 获得邮件主题
	 * @param msg 邮件内容
	 * @return 解码后的邮件主题
	 */ 
	 private String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException { 
	    return MimeUtility.decodeText(msg.getSubject()); 
	} 
	 
	/**
	 * 获得邮件发件人
	 * @param msg 邮件内容
	 * @return 姓名 <Email地址>
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */ 
	private String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException { 
	    String from = ""; 
	    Address[] froms = msg.getFrom(); 
	    if (froms.length < 1) 
	        throw new MessagingException("没有发件人!"); 
	     
	    InternetAddress address = (InternetAddress) froms[0]; 
	    String person = address.getPersonal(); 
	    if (person != null) { 
	        person = MimeUtility.decodeText(person) + " "; 
	    } else { 
	        person = ""; 
	    } 
	    from = person + "<" + address.getAddress() + ">"; 
	     
	    return from; 
	} 
	 
	/**
	 * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
	 * <p>Message.RecipientType.TO  收件人</p>
	 * <p>Message.RecipientType.CC  抄送</p>
	 * <p>Message.RecipientType.BCC 密送</p>
	 * @param msg 邮件内容
	 * @param type 收件人类型
	 * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
	 * @throws MessagingException
	 */ 
//	private String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException { 
//	    StringBuffer receiveAddress = new StringBuffer(); 
//	    Address[] addresss = null; 
//	    if (type == null) { 
//	        addresss = msg.getAllRecipients(); 
//	    } else { 
//	        addresss = msg.getRecipients(type); 
//	    } 
//	     
//	    if (addresss == null || addresss.length < 1) 
//	        throw new MessagingException("没有收件人!"); 
//	    for (Address address : addresss) { 
//	        InternetAddress internetAddress = (InternetAddress)address; 
//	        receiveAddress.append(internetAddress.toUnicodeString()).append(","); 
//	    } 
//	     
//	    receiveAddress.deleteCharAt(receiveAddress.length()-1); //删除最后一个逗号 
//	     
//	    return receiveAddress.toString(); 
//	} 
	 
	/**
	 * 获得邮件发送时间
	 * @param msg 邮件内容
	 * @return yyyy年mm月dd日 星期X HH:mm
	 * @throws MessagingException
	 */ 
//	private String getSentDate(MimeMessage msg, String pattern) throws MessagingException { 
//	    Date receivedDate = msg.getSentDate(); 
//	    if (receivedDate == null) 
//	        return ""; 
//	     
//	    if (pattern == null || "".equals(pattern)) 
//	        pattern = "yyyy年MM月dd日 E HH:mm "; 
//	     
//	    return new SimpleDateFormat(pattern).format(receivedDate); 
//	} 

	/**
     * 获得邮件文本内容
     * @param part 邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */ 
    private void getContent(Part part, StringBuffer content) throws MessagingException, IOException { 
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断 
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;  
        if (part.isMimeType("text/*") && !isContainTextAttach) { 
            content.append(part.getContent().toString()); 
        } else if (part.isMimeType("message/rfc822")) {  
        	getContent((Part)part.getContent(),content); 
        } else if (part.isMimeType("multipart/*")) { 
            Multipart multipart = (Multipart) part.getContent(); 
            int partCount = multipart.getCount(); 
            for (int i = 0; i < partCount; i++) { 
                BodyPart bodyPart = multipart.getBodyPart(i); 
                getContent(bodyPart,content); 
            } 
        } 
    } 
    
    /**
     * 文本解码
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */ 
    public static String decodeText(String encodeText) throws UnsupportedEncodingException { 
        if (encodeText == null || "".equals(encodeText)) { 
            return ""; 
        } else { 
            return MimeUtility.decodeText(encodeText); 
        } 
    } 
}
