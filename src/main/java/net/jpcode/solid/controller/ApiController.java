/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.JsonResult;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.spi.ResumeParser;
import net.jpcode.solid.spi.TextExtractor;
import net.jpcode.solid.spi.impl.ByteArrayContent;

/**
 * @author: billy zhang
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private ComponentFactory<ResumeParser> parserFactory;

	@Autowired
	private ComponentFactory<TextExtractor> extractorFactory;

	@PostMapping("parse")
	public JsonResult<Resume> parse(
			@RequestBody ParseData data) throws Exception {
		
		if (StringUtils.isEmpty(data.getFiledata())) {
			return JsonResult.fail(-1, "解析数据不能为空。");
		}

		String text = extractText(data.getFilename(), data.getFiledata());
		if (StringUtils.isEmpty(text)) {
			return JsonResult.fail(-1, "提取文本内容失败。");
		}
		
		Resume resume = parseResume(data.getFilename(), data.getFiledata(), text);
		
		if (resume != null) {
			return JsonResult.ok(resume);
		}
		
		return JsonResult.fail(-1, "未解析到简历。");
	}

	@PostMapping("extract")
	public JsonResult<String> extract(
			@RequestBody ParseData data) throws Exception {
		
		String text = extractText(data.getFilename(), data.getFiledata());
		
		if (!StringUtils.isEmpty(text)) {
			return JsonResult.ok(text);
		}
		
		return JsonResult.fail(-1, "未提取到文本内容。");
	}
	
	private Resume parseResume(String filename, String base64Data, String text) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(base64Data.getBytes("utf-8"));
		
		ByteArrayContent content = new ByteArrayContent();
		content.setName(filename);
		content.setData(bytes);
		content.setContent(text);
		
		return parserFactory.getDefault().parse(content);
	}
	
	private String extractText(String filename, String base64Data) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(base64Data.getBytes("utf-8"));
		
		int n = filename.lastIndexOf('.');
		if ( n > 0) {
			ByteArrayContent content = new ByteArrayContent();
			content.setName(filename);
			content.setFormat(filename.substring(n+1).toLowerCase());
			content.setData(bytes);
			return extractorFactory.getComponent(content.getFormat()).extract(content);
		}
		
		return "";
	}
}
