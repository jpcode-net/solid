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
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TextExtractorTest {

	@Autowired
	private ComponentFactory<TextExtractor> factory;
	
	@Test(timeout = 10000)
	public void testExtractHtml() throws Exception {
		String filename = "liyongjian.htm";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		String text = factory.getComponent("html").extract(new ResourceContent(filename, resource));
		
		assertNotNull(text);
	}
	
	@Test(timeout = 10000)
	public void testExtractPdf() throws Exception {
		String filename = "chenhewei.pdf";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		String text = factory.getComponent("pdf").extract(new ResourceContent(filename, resource));
		
		assertNotNull(text);
	}
	
	@Test(timeout = 10000)
	public void testExtractDoc() throws Exception {
		String filename = "wanglei.doc";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		String text = factory.getComponent("doc").extract(new ResourceContent(filename, resource));
		
		assertNotNull(text);
	}
	
}
