/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.service;

import java.util.List;

import net.jpcode.solid.model.TaskInfo;

/**
 * @author: billy zhang
 */
public interface TaskService {

	List<TaskInfo> selectTaskList();

	int updateTaskExecute(int taskId);
}
