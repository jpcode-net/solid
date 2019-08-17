/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.model.Resume;

/**
 * 简历解析
 * @author billy zhang
 *
 */
public interface ResumeParser extends Initialable {

	Resume parse(Content content) throws Exception;
	
}
