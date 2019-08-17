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
import net.jpcode.solid.schedule.job.EmailGatheJob;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailGatheJobTest {
	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper jsonMapper;

	@Ignore
	@Test(timeout = 300000)
	public void testExecute() {
		
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("taskId", 0);
		jobData.put("server", "pop.exmail.qq.com");
		jobData.put("port", 110);
		jobData.put("username", "aa");
		jobData.put("password", "bb");
		
		EmailGatheJob job = new EmailGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}
	
	@Test(timeout = 300000)
	public void testExecute2() throws IOException {

		String config = "{\"taskId\":0,\"server\":\"pop.exmail.qq.com\",\"port\":110,\"username\":\"zhangtao@3wchina.org\",\"password\":\"Zt654321\", \"filters\":{\"sender\":{\"mail\":\"wangxy@3wchina.net\"},\"fileext\":{\"ext\":\"doc,docx,pdf\"}}}";
		Map<String, Object> jobData = (Map<String, Object>)jsonMapper.readValue(config, Map.class);
		
		EmailGatheJob job = new EmailGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}
	
	@Test(timeout = 10000)
	public void testExecuteFail() {
		
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("taskId", 0);
		jobData.put("server", "pop.exmail.qq.com");
		jobData.put("port", 110);
		jobData.put("username", "aa");
		jobData.put("password", "");
		
		EmailGatheJob job = new EmailGatheJob();
		context.getAutowireCapableBeanFactory().autowireBean(job);
		
		job.execute(jobData);
	}
}
