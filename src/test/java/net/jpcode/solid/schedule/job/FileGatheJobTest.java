/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.schedule.job.FileGatheJob;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileGatheJobTest {
	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper jsonMapper;
	
	@Test(timeout = 10000)
	public void testExecuteFolder() {
		
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("taskId", 0);
		jobData.put("path", "/users/billyzh/tmp/");
		
		FileGatheJob job = new FileGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}

	@Test(timeout = 120000)
	public void testExecuteFolder2() throws IOException {
		
		String config = "{\"taskId\":0,\"path\":\"/users/billyzh/tmp\", \"filters\":{\"fileext\":{\"ext\":\"doc\"}}}";
		Map<String, Object> jobData = (Map<String, Object>)jsonMapper.readValue(config, Map.class);
		
		FileGatheJob job = new FileGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}
	
	@Ignore
	@Test(timeout = 10000)
	public void testExecuteFile() {
		
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("taskId", 0);
		jobData.put("path", "/users/billyzh/tmp/陈赫伟.pdf");
		
		FileGatheJob job = new FileGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}
}
