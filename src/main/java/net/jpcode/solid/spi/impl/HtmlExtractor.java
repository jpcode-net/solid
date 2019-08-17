/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import org.htmlcleaner.HtmlCleaner;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
public class HtmlExtractor implements TextExtractor {

	@Override
	public String extract(Content content) throws Exception {

		String html = content.getContent();
		
		HtmlCleaner clr = new HtmlCleaner();
		
		return clr.clean(html).getText().toString();
	}

}
