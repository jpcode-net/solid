/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import net.jpcode.solid.TaskSchedulerFactory;

/**
 * @author: billy zhang
 */
@Component
@ConditionalOnProperty(name="app.schedule.enable", havingValue="true")
public class SchedulerBean {

	private TaskScheduler scheduler;
	
	@Autowired
	private TaskSchedulerFactory schedulerFactory;
	
	@PostConstruct
    public void start() throws Exception {
    	scheduler = schedulerFactory.getScheduler();
    	
    	scheduler.start();
	}

    @PreDestroy
    public void stop() {
    	if (scheduler != null) {
	    	scheduler.stop();
	    	scheduler = null;
    	}
    }

}
