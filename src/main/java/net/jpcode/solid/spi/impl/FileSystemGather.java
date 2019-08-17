/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.extern.slf4j.Slf4j;
import net.jpcode.solid.ContentService;
import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.ContentFilter;
import net.jpcode.solid.spi.ContentGather;

/**
 * @author: billy zhang
 */
@Slf4j
public class FileSystemGather implements ContentGather {

	private String path;  // 文件或路径
	
	private ContentService service;
	private List<ContentFilter> filters = new ArrayList<ContentFilter>();
	
	@Override
	public void setContentService(ContentService service) {
		this.service = service;
	}
	
	@Override
	public void init(Map<String, Object> data) {
		path = (String)data.get("path");
		
		if (data.containsKey("filters")) {
			Map<String, Object> filterMap = (Map<String, Object>)data.get("filters");
			for (String name : filterMap.keySet()) {
				ContentFilter filter = service.createContentFilter(name);
				filter.init((Map<String, Object>)filterMap.get(name));
				filters.add(filter);
			}
		}
	}

	public List<Content> gathe() {

		List<Content> list = new ArrayList<>();
		
		File file = new File(path);
		if (file.isDirectory()) {
			String[] strs = file.list();
			for (String str : strs) {
				File f = new File(path + "/" + str);
				try {
					String uid = DigestUtils.md5Hex(new FileInputStream(f));
					if (service.selectContentCountByUID(uid) == 0) {
						FileContent content = new FileContent(uid, f);
						if (acceptContent(content)) {
							list.add(content);
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else {
			try {
				String uid = DigestUtils.md5Hex(new FileInputStream(file));
				if (service.selectContentCountByUID(uid) == 0) {
					FileContent content = new FileContent(uid, file);
					if (acceptContent(content)) {
						list.add(content);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
        		log.error(ex.getMessage(), ex);
			}
		}
		return list;
	}

	private boolean acceptContent(Content content) {
		for (ContentFilter filter : filters) {
			if (!filter.accept(content)) {
				return false;
			}
		}
		
		return true;
	}
}
