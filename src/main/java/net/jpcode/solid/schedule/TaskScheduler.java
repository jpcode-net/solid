/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule;

/**
 * @author: billy zhang
 */
public interface TaskScheduler {

	void start() throws Exception;
	
	void stop();
	
	void dispatch();
}
