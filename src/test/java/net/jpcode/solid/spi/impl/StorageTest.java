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

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.spi.Storage;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageTest {


	@Autowired
	private ComponentFactory<Storage> factory;
	
	@Test(timeout = 10000)
	public void testSave() throws IOException {
		
		String filename = "wanglei.doc";
		ClassPathResource resource = new ClassPathResource("file/" + filename);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		IOUtils.copy(resource.getInputStream(), outStream);
		
		factory.getDefault().save(filename, outStream.toByteArray());
		
	}
	
}
