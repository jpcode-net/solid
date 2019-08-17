/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.ContentService;
import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.ContentGather;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailGatherTest {

	@Autowired
	private ComponentFactory<ContentGather> factory;

	@Autowired
	private ContentService contentService;

	@Autowired
	private ObjectMapper jsonMapper;
	
	private ContentGather gather;
	
	@Before
	public void setUp() {

		gather = factory.getComponent("email");
	}

	@Ignore
	@Test(timeout = 120000)
	public void testGather() {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("server", "pop.exmail.qq.com");
		jobData.put("port", 110);
		jobData.put("username", "aa");
		jobData.put("password", "bb");
		
		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list = gather.gathe();
		
		assertNotNull(list);
		//assertTrue(list.size() == 0);
	}
	
	@Test(timeout = 120000)
	public void testGatherWithFilter() throws IOException {

		String config = "{\"server\":\"pop.exmail.qq.com\",\"port\":110,\"username\":\"zhangtao@3wchina.org\",\"password\":\"Zt654321\", \"filters\":{\"sender\":{\"mail\":\"wangxy@3wchina.net\"},\"fileext\":{\"ext\":\"doc,docx,pdf\"}}}";
		Map<String, Object> jobData = (Map<String, Object>)jsonMapper.readValue(config, Map.class);
		
		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list = gather.gathe();
		
		assertNotNull(list);
		for (Content cont : list) {
			assertEquals("doc", cont.getFormat());
		}
	}
	
	@Test(timeout = 120000)
	public void testGatherFail() {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("server", "pop.exmail.qq.com");
		jobData.put("port", 110);
		jobData.put("username", "aa");
		jobData.put("password", "");
		
		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list = gather.gathe();
		
		assertNotNull(list);
		//assertTrue(list.size() == 0);
	}
	
}
