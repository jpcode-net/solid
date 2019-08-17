/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import net.jpcode.solid.model.Content;

/**
 * @author: billy zhang
 */
class FileContent implements Content {

	private final String uid;
	private final File file;
	private final String format;
	private String content;
	
	public FileContent(String uid, File file) {
		this.uid = uid;
		this.file = file;
		int n = file.getName().lastIndexOf(".");
		if (n > 0) {
			format = file.getName().substring(n+1).toLowerCase();
		} else {
			format = "unknown"; // TODO:???
		}
	}
	
	@Override
	public String getUid() {
		return uid;
	}
	
	@Override
	public String getName() {
		return file.getName();
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
	public String getFormat() {
		return format;
	}

	@Override
	public byte[] getData() throws IOException {
		
		FileInputStream inStream = new FileInputStream(file);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		IOUtils.copy(inStream, outStream);
		return outStream.toByteArray();
		
	}
}
