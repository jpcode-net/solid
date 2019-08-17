/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.ByteArrayInputStream;

import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
public class WordExtractor implements TextExtractor {

	@Override
	public String extract(Content content) throws Exception {

    	ByteArrayInputStream is = new ByteArrayInputStream(content.getData());
		
    	POITextExtractor extract = ExtractorFactory.createExtractor(is);
		
    	return extract.getText();
       
//        	try {
//	        	org.apache.poi.hwpf.extractor.WordExtractor ex = new org.apache.poi.hwpf.extractor.WordExtractor(is);
//	            text = ex.getText();
//	            ex.close();
//        	} catch (Exception e2) {
//        		e2.printStackTrace();
//        	}
	}

}
