/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.model;

import java.util.Date;

/**
 * @author: billy zhang
 */
public class TaskInfo {

	private int id;
	private String name;
	private String jobClazz;
	private String jobData;
	private String cronExp;
	private Date modified;
	private int del;
	private int executeCount;
	private Date lastExecute;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobClazz() {
		return jobClazz;
	}

	public void setJobClazz(String jobClazz) {
		this.jobClazz = jobClazz;
	}

	public String getJobData() {
		return jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	
	public int getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
	}

	public Date getLastExecute() {
		return lastExecute;
	}

	public void setLastExecute(Date lastExecute) {
		this.lastExecute = lastExecute;
	}
	
}
