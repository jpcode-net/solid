/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author: billy zhang
 */
public class ResUtil {
	
	public static byte[] getData(Resource res) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		IOUtils.copy(res.getInputStream(), outStream);
		byte[] bytes = outStream.toByteArray();
		outStream.close();
		res.getInputStream().close();
		return bytes;
	}
	
	public static byte[] getData(String resfile) throws IOException {

		ClassPathResource res = new ClassPathResource("file/" + resfile);
		return getData(res);
		
	}
	
}
