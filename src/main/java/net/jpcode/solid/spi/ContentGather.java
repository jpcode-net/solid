/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi;

import java.util.List;

import net.jpcode.solid.ContentService;
import net.jpcode.solid.model.Content;

/**
 * 内容收集接口
 * @author billy zhang
 *
 */
public interface ContentGather extends Initialable {

	void setContentService(ContentService service);
	
	List<Content> gathe();
	
}
