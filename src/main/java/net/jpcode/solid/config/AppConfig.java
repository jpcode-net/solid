/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author billy zhang
 * 
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

	private Map<String, Map<String, Object>> config;
	
	public void setConfig(Map<String, Map<String, Object>> config) {
		this.config = config;
	}
	
	public Map<String, Map<String, Object>> getConfig() {
		return config;
	}

}
