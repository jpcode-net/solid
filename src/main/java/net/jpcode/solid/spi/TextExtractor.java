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
 * 文本提取
 * @author billy zhang
 *
 */
public interface TextExtractor {

	String extract(Content content) throws Exception;
}
