/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import net.jpcode.solid.spi.Storage;

/**
 * @author: billy zhang
 */
public class LocalStorage implements Storage {

	private String root;
	

	@Override
	public void init(Map<String, Object> data) {
		this.root = (String)data.get("root");
	}

	@Override
	public void save(String filename, byte[] data) {
		String fullname = root + "/" + filename;
		
		try {
			FileOutputStream outStream = new FileOutputStream(fullname);
			outStream.write(data);
			outStream.flush();
			outStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
