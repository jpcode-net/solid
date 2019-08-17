/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.Resource;

import net.jpcode.solid.model.Content;

/**
 * @author: billy zhang
 */
public class ResourceContent implements Content {

	private final String filename;
	private final String format;
	private final Resource resource;
	private String content;
	
	public ResourceContent(String filename, Resource resource) {
		this.filename = filename;
		this.resource = resource;
		
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
	public String getContent() throws IOException {

		if (content == null) {
			byte[] bytes = ResUtil.getData(resource);
			content = new String(bytes, "utf-8");
		}
		
		return content;
	}
	
	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public byte[] getData() throws IOException {

		return ResUtil.getData(resource);
		
	}
	
}
