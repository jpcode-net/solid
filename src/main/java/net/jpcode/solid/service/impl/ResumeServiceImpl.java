/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.ContentService;
import net.jpcode.solid.dao.ResumeDao;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.model.ResumeData;
import net.jpcode.solid.service.ResumeService;
import net.jpcode.solid.spi.ContentFilter;

/**
 * @author: billy zhang
 */
@Service
public class ResumeServiceImpl implements ResumeService, ContentService {

	@Autowired
	private ResumeDao resumeDao;

	@Autowired
	private ComponentFactory<ContentFilter> filterFactory;
	
	@Override
	public ContentFilter createContentFilter(String name) {
		return filterFactory.getComponent(name);
	}
	
	@Override
	public int selectContentCountByUID(String uid) {
		return resumeDao.selectContentCountByUID(uid);
	}
	
	@Override
	public void insertResumeData(ResumeData data) {
		resumeDao.insertResumeData(data);
	}

	@Override
	public int updateResumeData(ResumeData data) {
		return resumeDao.updateResumeData(data);
	}

	@Override
	public void insertResume(Resume resume) {
		resumeDao.insertResume(resume);
	}

}
