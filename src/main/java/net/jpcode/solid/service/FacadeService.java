/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.model.ResumeData;
import net.jpcode.solid.spi.Storage;

/**
 * @author: billy zhang
 */
@Service
public class FacadeService {

	@Autowired
	private ComponentFactory<Storage> storageFactory;
	
	@Autowired
	private ResumeService resumeService;
	
	@Autowired
	private TaskService taskService;
	
	public void insertResumeData(ResumeData data) {
		
		resumeService.insertResumeData(data);
		
		if (data.getData() != null) {
			//storageFactory.getDefault().save(data.getTitle(), data.getData());
		}
	}
	
	public int updateResumeData(ResumeData data) {
		
		return resumeService.updateResumeData(data);
	}

	public void saveResume(Resume resume) {
		
		// TODO: 查重？？
		resumeService.insertResume(resume);
	}

	public void updateTaskExecute(int taskId) {
		taskService.updateTaskExecute(taskId);
	}


}
