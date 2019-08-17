/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.spi.impl.ResUtil;
import net.jpcode.solid.JsonResult;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.util.CryptoUtils;

/**
 * @author: billy zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTest {

	private static final String SECRET_KEY = "abcdef123456";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	protected MockMvc mvc;
	
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		mvc.getDispatcherServlet().setThrowExceptionIfNoHandlerFound(true);
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test(timeout = 10000)
	public void test_methodNotSupport() throws Exception {
		mvc.perform(get("/api/parse"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	
	@Test(timeout = 10000)
	public void test_methodNotExists() throws Exception {
		mvc.perform(get("/api/parse3"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	
	@Test(timeout = 10000)
	public void test_methodNotExists2() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		MockHttpServletRequestBuilder builder = get(String.format("/api/parse3?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		mvc.perform(builder)
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	
	@Test(timeout = 10000)
	public void test_nosign() throws Exception {
		mvc.perform(post("/api/parse"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test(timeout = 10000)
	public void test_errsign() throws Exception {
		mvc.perform(post("/api/parse?ts=1&sign=2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test(timeout = 10000)
	public void testParse_noparams() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/parse?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
				
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test//(timeout = 10000)
	public void testParse_withparams() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		byte[] data = ResUtil.getData("wanglei.doc");

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "wanglei.doc");
		dataMap.put("filedata", Base64.getEncoder().encodeToString(data));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/parse?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		String response = mvc.perform(builder)
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		JavaType javaType = jsonMapper.getTypeFactory().constructParametricType(JsonResult.class, Resume.class);
		JsonResult<Resume> jsonRet = jsonMapper.readValue(response, javaType);

		assertEquals(jsonRet.getErrMsg(), 0, jsonRet.getErrCode());
		
		Resume resume = jsonRet.getData();
		assertNotNull("没有简历数据。", resume);
		assertNotNull("没有解析到姓名", resume.getName());
	}
	
	@Test(timeout = 10000)
	public void testParse_null() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "null");
		dataMap.put("filedata", Base64.getEncoder().encodeToString("test.doc".getBytes("utf-8")));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/parse?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test(timeout = 10000)
	public void testExtract_noparams() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
				
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test(timeout = 10000)
	public void testExtract_pdf() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		byte[] data = ResUtil.getData("chenhewei.pdf");
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "chenhewei.pdf");
		dataMap.put("filedata", Base64.getEncoder().encodeToString(data));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		String response = mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(0))
			.andReturn().getResponse().getContentAsString();
		
		JavaType javaType = jsonMapper.getTypeFactory().constructParametricType(JsonResult.class, String.class);
		JsonResult<String> jsonRet = jsonMapper.readValue(response, javaType);

		assertNotNull("没有文本内容。", jsonRet.getData());
	}
	

	@Test(timeout = 10000)
	public void testExtract_doc() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		byte[] data = ResUtil.getData("wanglei.doc");
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "wanglei.doc");
		dataMap.put("filedata", Base64.getEncoder().encodeToString(data));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		String response = mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(0))
			.andReturn().getResponse().getContentAsString();
		
		JavaType javaType = jsonMapper.getTypeFactory().constructParametricType(JsonResult.class, String.class);
		JsonResult<String> jsonRet = jsonMapper.readValue(response, javaType);

		assertNotNull("没有文本内容。", jsonRet.getData());
	}
	

	@Test(timeout = 10000)
	public void testExtract_formatnotsupport() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		byte[] data = ResUtil.getData("chenhewei.pdf");
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "chenhewei.pd");
		dataMap.put("filedata", Base64.getEncoder().encodeToString(data));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
	

	@Test(timeout = 10000)
	public void testExtract_noformat() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		byte[] data = ResUtil.getData("chenhewei.pdf");
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "chenhewei");
		dataMap.put("filedata", Base64.getEncoder().encodeToString(data));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}

	@Test(timeout = 10000)
	public void testExtract_dataerror() throws Exception {
		String ts = new Date().getTime() / 1000 + "";
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("filename", "test.pdf");
		dataMap.put("filedata", Base64.getEncoder().encodeToString("abcdef123456".getBytes("utf-8")));
		
		MockHttpServletRequestBuilder builder = post(String.format("/api/extract?ts=%s&sign=%s", ts, CryptoUtils.MD5(planStr)));
		builder.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonMapper.writeValueAsBytes(dataMap));
		
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errCode").value(-1));
	}
}
