/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi;

import java.util.Map;

/**
 * @author: billy zhang
 */
public interface Initialable {

	void init(Map<String, Object> data);
}
