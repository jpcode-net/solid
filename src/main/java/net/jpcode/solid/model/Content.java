/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.model;

import java.io.IOException;

/**
 * @author: billy zhang
 */
public interface Content {

	String getUid();
	
	String getName();
	
	String getFormat();
	
	void setContent(String content);
	
	String getContent() throws IOException;
	
	byte[] getData() throws IOException;
	
}
