/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.spi.impl;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.model.Content;
import net.jpcode.solid.model.Resume;
import net.jpcode.solid.spi.ResumeParser;

/**
 * @author: billy zhang
 */
public class ResumeSDKParser implements ResumeParser {

	private String url;
	private int uid;
	private String pwd;
	private String auth;

	private ObjectMapper jsonMapper = new ObjectMapper();

	@Override
	public void init(Map<String, Object> config) {

		this.url = (String)config.get("url");
		this.uid = Integer.parseInt( config.get("uid").toString() );
		this.pwd = config.get("pwd").toString();
		this.auth = (String)config.get("auth");
	}
	
	@Override
	public Resume parse(Content content) throws Exception {

		byte[] cntnData = null;
		if (content.getData() != null) {
			cntnData = content.getData();
		} else {
			cntnData = content.getContent().getBytes("utf-8");
		}
		
		return parse(content.getName(), cntnData);
	}
	
	public Resume parse(String filename, byte[] bytes) throws Exception {
		
		Map<String, Object> data = new HashMap<>();
		data.put("uid", uid);
		data.put("pwd", pwd);
		data.put("fname", filename);
		
		data.put("base_cont", Base64.getEncoder().encodeToString(bytes) );
		
		String authInfo = Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("Authorization", "Basic " + authInfo);
		
		httpPost.setEntity(new StringEntity( jsonMapper.writeValueAsString(data) ));
		 
		CloseableHttpResponse response = httpClient.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		String respContent = EntityUtils.toString(entity, "UTF-8"); 
		
		response.close();
		httpClient.close();
		
		if (statusCode == 200) {
			return getResume(respContent);
		}
			
		return null;
	}

	private Resume getResume(String response) throws IOException {
		Map<String, Object> respMap = jsonMapper.readValue(response, Map.class);
		if (respMap.containsKey("result")) {
			Map<String, String> resultMap = (Map<String, String>)respMap.get("result");
			
			Resume resume = new Resume();
			resume.setOriginalContent(response);
			
			resume.setName( getValue(resultMap, "name") );
			resume.setPhone( getValue(resultMap, "phone") );
			resume.setEmail( getValue(resultMap, "email") );
			resume.setQq( getValue(resultMap, "qq") );
			resume.setExpectJob( getValue(resultMap, "expect_job") );
				
			return resume;
		}
		
		return null;
	}
	
	private String getValue(Map<String, String> map, String name) throws IOException {
		String value = "";
		if (map.containsKey(name)) {
			value = URLDecoder.decode(map.get(name), "utf-8");
		}
		return value;
	}
}
