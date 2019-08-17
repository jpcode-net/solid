/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi;

/**
 * @author: billy zhang
 */
public interface Storage extends Initialable {

	void save(String filename, byte[] data);
}
