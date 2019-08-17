/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.spi.ResumeParser;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResumeSDKParserTest {
	
	@Autowired
	private ComponentFactory<ResumeParser> parserFactory;

	@Autowired
	private ComponentFactory<TextExtractor> extractorFactory;
	

	@Test(timeout = 30000)
	public void testParseForData() throws Exception {

		String filename = "wanglei.doc";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		Resume resume = parserFactory.getComponent("resumesdk").parse(new ResourceContent(filename, resource));
		
		assertNotNull(resume);
		assertNotNull(resume.getName());
		
	}
	

	@Test(timeout = 30000)
	public void testParseForContent() throws Exception {

		String filename = "wanglei.doc";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		String text = extractorFactory.getComponent("doc").extract(new ResourceContent(filename, resource));
		assertNotNull(text);
		
		Resume resume = parserFactory.getComponent("resumesdk").parse(new TextContent("wanglei.txt", text));
		assertNotNull(resume);
		assertNotNull(resume.getName());
		
	}
	
}
