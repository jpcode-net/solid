/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi;

import net.jpcode.solid.model.Content;

/**
 * 内容过滤接口
 * @author: billy zhang
 */
public interface ContentFilter extends Initialable {

	boolean accept(Content content);
	
}
