/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.spi.ContentFilter;
import net.jpcode.solid.spi.impl.ByteArrayContent;
import net.jpcode.solid.spi.impl.MessageContent;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileExtFilterTest {

	@Autowired
	private ComponentFactory<ContentFilter> filterFactory;
	
	@Before
	public void setUp() {

	}
	
	@Test(timeout = 10000)
	public void testAcceptTrue1() {

		TextContent content = new TextContent("test.doc", "abc123");
		
		ContentFilter filter = filterFactory.getComponent("fileext");
		
		Map<String, Object> config = new HashMap<>();
		config.put("ext", "doc,docx,pdf");
		filter.init(config);
		assertTrue(filter.accept(content));
		
	}
	
	@Test(timeout = 10000)
	public void testAcceptFalse1() {
		
		Map<String, Object> config = new HashMap<>();
		config.put("ext", "doc,docx,pdf");
		
		ContentFilter filter = filterFactory.getComponent("fileext");
		filter.init(config);

		TextContent content = new TextContent("test.txt", "abc123");
		
		assertFalse(filter.accept(content));
	}
	
}
