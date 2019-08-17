/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

import java.util.Map;

/**
 * @author: billy zhang
 */
public class DispatchJob {

	public void execute(Map<String, Object> jobData) {
		TaskScheduler scheduler = (TaskScheduler)jobData.get("scheduler");
		scheduler.dispatch();
	}

}
