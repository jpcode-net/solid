/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.ContentService;
import net.jpcode.solid.model.Content;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.model.ResumeData;
import net.jpcode.solid.service.FacadeService;
import net.jpcode.solid.spi.ContentGather;
import net.jpcode.solid.spi.ResumeParser;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
@Slf4j
public abstract class GatheJob {

	@Autowired
	private ComponentFactory<ContentGather> gatherFactory;
	
	@Autowired
	private ComponentFactory<TextExtractor> extractorFactory;
	
	@Autowired
	private ComponentFactory<ResumeParser> parserFactory;
	
	@Autowired
	private FacadeService facadeService;

	@Autowired
	private ContentService contentService;
	
	protected abstract String getGatherName(); 
	
	private volatile List<Integer> runningTask = new ArrayList<>();
	
	protected void internalExecute(Map<String, Object> jobData) {
		// 初始化
		ContentGather gather = gatherFactory.getComponent(getGatherName());
		gather.setContentService(contentService);
		gather.init(jobData);
		
		// 采集
		List<Content> cntnlist = gather.gathe();
		
		for (Content cntn : cntnlist) {
			try {
				// 保存数据
				ResumeData data = saveContent(cntn);
				
				// 提取文本
				String text = extractText(cntn);
				if (!StringUtils.isEmpty(text)) {
					
					data.setText( text );
					facadeService.updateResumeData(data);
					
					// TODO: 用于只支持解析文本的简历解析器，refactor...
					if (cntn.getContent() == null ||
							cntn.getContent().trim().equals("")) {
						cntn.setContent(text);
					}
				}
	
				// 解析
				Resume resume = parseResume(cntn);
				if (resume != null) {
					resume.setDataId(data.getId());
					resume.setAddTime(new Date());
					facadeService.saveResume(resume);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage(), ex);
			}
		}
	}
	
	public final void execute(Map<String, Object> jobData) {

		Integer taskId = (Integer)jobData.get("taskId");
		
		if (runningTask.contains(taskId)) {// 如还在执行，则本次不执行。
			log.info("last job is running.");
			return;
		}
		
		try {
			Collections.synchronizedList(runningTask).add(taskId);  // 是否需要使用同步对象?? 
			
			facadeService.updateTaskExecute(taskId);
			
			internalExecute(jobData);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
		} finally {
			Collections.synchronizedList(runningTask).remove(taskId); // 是否需要使用同步对象?? 
		}
	}
	
	private ResumeData saveContent(Content cntn) throws IOException {
		
		ResumeData data = new ResumeData();
		data.setUid(cntn.getUid());
		data.setTitle(cntn.getName());
		data.setContent(cntn.getContent());
		data.setData(cntn.getData());
		data.setFormat(cntn.getFormat());
		data.setKind(getGatherName());
		data.setAddTime(new Date());
		
		facadeService.insertResumeData(data);
		
		return data;
	}

	private String extractText(Content cntn) throws Exception {
		TextExtractor extractor = extractorFactory.getComponent(cntn.getFormat());
		if (extractor != null) {
			return extractor.extract(cntn);
		}
		
		return "";
	}
	
	private Resume parseResume(Content cntn) throws Exception {
		ResumeParser parser = parserFactory.getDefault();
		if (parser != null) {
			return parser.parse(cntn);
		}
		return null;
	}
}
