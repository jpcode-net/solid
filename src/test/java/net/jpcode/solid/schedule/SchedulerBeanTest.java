/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import net.jpcode.solid.TaskSchedulerFactory;
import net.jpcode.solid.schedule.SchedulerBean;
import net.jpcode.solid.schedule.TaskScheduler;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerBeanTest {

	@Autowired
	private TaskSchedulerFactory schedulerFactory;
	
	@Autowired
	private ApplicationContext context;
	
	private TaskScheduler originalScheduler;
	private MockTaskScheduler mockScheduler;
	
	@Before
	public void setUp() {
		originalScheduler = schedulerFactory.getScheduler();

		mockScheduler = new MockTaskScheduler();
		
		schedulerFactory.setScheduler(mockScheduler);
	}
	
	@After
	public void clean() {
		schedulerFactory.setScheduler(originalScheduler);
	}
	
	@Test
	public void testSchedulerBean() throws Exception {
		
        SchedulerBean bean = context.getAutowireCapableBeanFactory().createBean(SchedulerBean.class);
        assertTrue(mockScheduler.isStarted());
        
		Thread.sleep(2000);
		
		context.getAutowireCapableBeanFactory().destroyBean(bean);
		assertTrue(mockScheduler.isStopped());
	}
	
	private class MockTaskScheduler implements TaskScheduler {

		private boolean started;
		private boolean stopped;
		
		@Override
		public void start() throws Exception {
			started=true;
		}

		@Override
		public void stop() {
			stopped=true;
			started=false;
		}

		@Override
		public void dispatch() {
			
		}
		
		public boolean isStarted() {
			return started;
		}

		public boolean isStopped() {
			return stopped;
		}
		
	}
}
