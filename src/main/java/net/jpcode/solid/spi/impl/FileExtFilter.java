/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.ContentFilter;

/**
 * 文件后缀过滤
 * @author: billy zhang
 */
public class FileExtFilter implements ContentFilter {

	private List<String> extlist = new ArrayList<>();
	
	@Override
	public void init(Map<String, Object> data) {
		String[] strs = ((String)data.get("ext")).split(",");
		for (int i=0; i<strs.length; i++) {
			extlist.add(strs[i]);
		}
	}

	@Override
	public boolean accept(Content content) {

		if (content instanceof MessageContent)  // TODO: refactor...
			return true;
		
		String format = content.getFormat();
	
		return extlist.contains(format);
		
	}

}
