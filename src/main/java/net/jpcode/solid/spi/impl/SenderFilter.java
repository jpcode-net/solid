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
 * 发件人过滤
 * @author: billy zhang
 */
public class SenderFilter implements ContentFilter {

	private List<String> maillist;
	
	@Override
	public void init(Map<String, Object> data) {
		maillist = new ArrayList<>();
		String[] strs = ((String)data.get("mail")).split(",");
		for (int i=0; i<strs.length; i++) {
			String str = strs[i].trim();
			if (str.length() > 0) {
				maillist.add(str);
			}
		}
	}

	@Override
	public boolean accept(Content content) {

		if (!(content instanceof MessageContent)) {  // TODO: refactor...
			return true;
		}
		
		MessageContent msgContent = (MessageContent)content;
		for (String mail : maillist) {
			if (msgContent.getFrom().indexOf(mail) != -1) {
				return true;
			}
		}
		
		return false;
	}

}
