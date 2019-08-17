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
public class ByteArrayContent implements Content {

	private String name;
	private String format;
	private String content;
	private byte[] data;
	
	@Override
	public String getUid() {
		return UUID.randomUUID().toString();
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
	public byte[] getData() {
		return data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
}
