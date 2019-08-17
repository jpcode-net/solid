/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.service;

import net.jpcode.solid.model.Resume;
import net.jpcode.solid.model.ResumeData;

/**
 * @author: billy zhang
 */
public interface ResumeService {

	void insertResumeData(ResumeData data);

	int updateResumeData(ResumeData data);

	void insertResume(Resume resume);

}
