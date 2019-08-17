/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.model.TaskInfo;
import net.jpcode.solid.schedule.DefaultTaskScheduler;
import net.jpcode.solid.schedule.DispatchJob;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultTaskSchedulerTest {

	private DefaultTaskScheduler scheduler;
	
	@Autowired
	private ObjectMapper jsonMapper;

	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() {
		scheduler = new DefaultTaskScheduler();
		context.getAutowireCapableBeanFactory().autowireBean(scheduler);
	}
	
	@After
	public void clean() {
		scheduler.stop();
	}
	
	@Test(timeout = 10000)
	public void testStart() throws Exception {
		scheduler.start();
		assertEquals(1, scheduler.getTaskCount());
		
	}

	@Test(timeout = 10000)
	public void testDispatch() throws Exception {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("scheduler", scheduler);
		
		DispatchJob dispJob = new DispatchJob();
		dispJob.execute(jobData);

		assertEquals(2, scheduler.getTaskCount());
	}
	
	@Test(timeout = 10000)
	public void testAddJob() throws IOException {

		scheduler.dispatch();
		assertEquals(2, scheduler.getTaskCount());
		
		List<TaskInfo> tasklist = new ArrayList<>();
		tasklist.addAll(deepCopy(scheduler.getTaskList(), TaskInfo.class));
		
		TaskInfo ti = new TaskInfo();
		ti.setName("filesystem2");
		ti.setJobClazz("net.jpcode.resume.schedule.job.FileGatheJob");
		ti.setCronExp("0 0/1 * * * ?");
		ti.setDel(0);
		ti.setModified(new Date());
		
		Map<String, Object> conf = new HashMap<>();
		conf.put("path", "/users/billyzh/tmp2/");
		ti.setJobData( jsonMapper.writeValueAsString(conf) );
		
		tasklist.add(ti);
		scheduler.scheduleTasks(tasklist);
		
		assertEquals(3, scheduler.getTaskCount());
	}

	@Test(timeout = 10000)
	public void testUpdateJob() throws IOException  {
		scheduler.dispatch();
		assertEquals(2, scheduler.getTaskCount());
		
		List<TaskInfo> tasklist = new ArrayList<>();
		tasklist.addAll(deepCopy(scheduler.getTaskList(), TaskInfo.class));
		
		tasklist.get(0).setModified(new Date());
		scheduler.scheduleTasks(tasklist);
		
		assertEquals(2, scheduler.getTaskCount());
	}
	
	@Test(timeout = 10000)
	public void testDeleteJob() throws IOException {
		scheduler.dispatch();
		assertEquals(2, scheduler.getTaskCount());
		
		List<TaskInfo> tasklist = new ArrayList<>();
		tasklist.addAll(deepCopy(scheduler.getTaskList(), TaskInfo.class));

		tasklist.get(0).setDel(1);
		scheduler.scheduleTasks(tasklist);
		
		assertEquals(1, scheduler.getTaskCount());
	}
	
	public <T> Collection<T> deepCopy(Collection<T> src, Class<T> clazz) throws IOException {  
	      
	    byte[] bytes = jsonMapper.writeValueAsBytes(src);
	    
	    JavaType javaType = jsonMapper.getTypeFactory().constructParametricType(Collection.class, clazz);
	    return jsonMapper.readValue(bytes, javaType);
	
	}  
}
