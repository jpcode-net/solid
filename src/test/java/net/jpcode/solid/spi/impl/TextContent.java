/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.util.UUID;

import net.jpcode.solid.model.Content;

/**
 * @author: billy zhang
 */
public class TextContent implements Content {

	private final String filename;
	private final String format;
	private final String text;
	
	public TextContent(String filename, String text) {
		this.filename = filename;
		this.text = text;
		
		int n = filename.lastIndexOf('.');
		if (n > 0) {
			format = filename.substring(n+1);
		} else {
			format = "unknown";
		}
	}
	
	@Override
	public String getUid() {
		return UUID.randomUUID().toString();
	}
	
	@Override
	public String getName() {
		return filename;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public String getContent() {
		return text;
	}

	@Override
	public void setContent(String content) {
		
	}
	
	@Override
	public byte[] getData() {
		return null;
	}
	
}
