/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.schedule.job;

/**
 * @author: billy zhang
 */
public class FileGatheJob extends GatheJob {

	@Override
	protected String getGatherName() {
		return "filesystem";
	}
	
}
