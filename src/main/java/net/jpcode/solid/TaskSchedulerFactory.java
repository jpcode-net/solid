/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid;

import net.jpcode.solid.schedule.TaskScheduler;

/**
 * @author: billy zhang
 */
public class TaskSchedulerFactory {

	private TaskScheduler scheduler;

	public TaskScheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(TaskScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	
}
