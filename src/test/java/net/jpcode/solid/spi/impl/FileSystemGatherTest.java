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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
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
public class FileSystemGatherTest {

	@Autowired
	private ComponentFactory<ContentGather> factory;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	private ContentGather gather;
	
	@Before
	public void setUp() {

		gather = factory.getComponent("filesystem");
	}
	
	@Test(timeout = 10000)
	public void testGatherFolder() {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("path", "/users/billyzh/tmp");
		
		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list =gather.gathe();
		
		assertNotNull(list);
		//assertTrue("没有文件。", list.size() > 0);
	}
	
	@Test(timeout = 10000)
	public void testGatherFolderWithFilter() throws IOException {
		
		String config = "{\"path\":\"/users/billyzh/tmp\", \"filters\":{\"fileext\":{\"ext\":\"doc\"}}}";
		Map<String, Object> jobData = (Map<String, Object>)jsonMapper.readValue(config, Map.class);
		
		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list =gather.gathe();
		
		assertNotNull(list);
		List<String> extlist = new ArrayList<>();
		extlist.add("doc");
		extlist.add("docx");
		extlist.add("pdf");
		
		for (Content cont : list) {
			assertTrue(extlist.contains(cont.getFormat()));
		}
	}
	
	@Test(timeout = 10000)
	public void testGatherFile() {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("path", "/users/billyzh/tmp/陈赫伟.pdf");

		gather.setContentService(contentService);
		gather.init(jobData);
		List<Content> list =gather.gathe();
		
		assertNotNull(list);
		//assertTrue("没有文件。", list.size() == 1);
	}
}
