/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author: billy zhang
 */
public class PdfExtractor implements TextExtractor {

	@Override
	public String extract(Content content) throws Exception {
		
		String text = "";
		PDDocument pdfdoc = null;
		
		try {
			pdfdoc = PDDocument.load(content.getData());
			PDFTextStripper stp = new PDFTextStripper();
			text = stp.getText(pdfdoc);
			
		} finally {
			if (pdfdoc != null) {
				pdfdoc.close();
			}
		}
		
		return text;
	}

}
