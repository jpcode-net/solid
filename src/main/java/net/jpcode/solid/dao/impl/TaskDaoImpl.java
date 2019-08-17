/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.jpcode.solid.dao.TaskDao;
import net.jpcode.solid.model.TaskInfo;

@Repository
public class TaskDaoImpl implements TaskDao {

	@Override
	public List<TaskInfo> selectTaskList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateTaskExecute(int taskId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
