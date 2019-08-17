/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jpcode.solid.dao.TaskDao;
import net.jpcode.solid.model.TaskInfo;
import net.jpcode.solid.service.TaskService;

/**
 * @author: billy zhang
 */
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	@Override
	public List<TaskInfo> selectTaskList() {
		return taskDao.selectTaskList();
	}

	@Override
	public int updateTaskExecute(int taskId) {
		return taskDao.updateTaskExecute(taskId);
	}

}
