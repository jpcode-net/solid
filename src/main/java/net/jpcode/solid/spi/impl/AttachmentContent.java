/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import net.jpcode.solid.model.Content;

/**
 * @author: billy zhang
 */
class AttachmentContent implements Content {

	private final String uid;
	private final String name;
	private final String format;
	private final byte[] fileData;
	private String content;
	
	public AttachmentContent(String uid, String name, byte[] fileData) {
		this.uid = uid;
		this.name = name;
		this.fileData = fileData;
		
		int n = name.lastIndexOf(".");
		if (n > 0) {
			format = name.substring(n+1).toLowerCase();
		} else {
			format = "unknown"; // TODO:???
		}
	}
	
	public String getUid() {
		return uid;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getFormat() {
		return format;
	}
	
	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public byte[] getData() {
		return fileData;
	}
}
