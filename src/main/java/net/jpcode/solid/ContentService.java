/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid;

import net.jpcode.solid.spi.ContentFilter;

/**
 * @author: billy zhang
 */
public interface ContentService {

	int selectContentCountByUID(String uid);

	ContentFilter createContentFilter(String name);

}
