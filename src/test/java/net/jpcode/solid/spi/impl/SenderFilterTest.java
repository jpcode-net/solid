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
public class SenderFilterTest {

	@Autowired
	private ComponentFactory<ContentFilter> filterFactory;
	
	@Before
	public void setUp() {

	}
	
	@Test(timeout = 10000)
	public void testAcceptTrue1() {

		MessageContent content = new MessageContent("");
		content.setFrom("zhangtao@3wchina.org");
		
		ContentFilter filter = filterFactory.getComponent("sender");
		
		Map<String, Object> config = new HashMap<>();
		config.put("mail", "zhangtao@3wchina.org, a12@abc.com");
		filter.init(config);
		assertTrue(filter.accept(content));
		
	}
	
	@Test(timeout = 10000)
	public void testAcceptTrue2() {

		MessageContent content = new MessageContent("");
		content.setFrom("zhangtao@3wchina.org");
		
		ContentFilter filter = filterFactory.getComponent("sender");
		
		Map<String, Object>  config = new HashMap<>();
		config.put("mail", "zhangtao@3wchina.org , a12@abc.com");
		filter.init(config);
		assertTrue(filter.accept(content));
		
	}
	
	@Test(timeout = 10000)
	public void testAcceptTrue3() {

		MessageContent content = new MessageContent("");
		content.setFrom("a12@abc.com");
		
		ContentFilter filter = filterFactory.getComponent("sender");
		
		Map<String, Object> config = new HashMap<>();
		config.put("mail", "zhangtao@3wchina.org, , a12@abc.com");
		filter.init(config);
		assertTrue(filter.accept(content));
		
	}
	
	@Test(timeout = 10000)
	public void testAcceptTrue4() {
		
		Map<String, Object> config = new HashMap<>();
		config.put("mail", "zhangtao@3wchina.org , a12@abc.com");
		
		ContentFilter filter = filterFactory.getComponent("sender");
		filter.init(config);
		
		ByteArrayContent content = new ByteArrayContent();
		
		assertTrue(filter.accept(content));
	}
	
	@Test(timeout = 10000)
	public void testAcceptFalse1() {
		
		Map<String, Object> config = new HashMap<>();
		config.put("mail", "zhangtao@3wchina.org , a12@abc.com");
		
		ContentFilter filter = filterFactory.getComponent("sender");
		filter.init(config);
		
		MessageContent content = new MessageContent("");
		content.setFrom("billy_zh@126.com");
		
		assertFalse(filter.accept(content));
	}
	
}
