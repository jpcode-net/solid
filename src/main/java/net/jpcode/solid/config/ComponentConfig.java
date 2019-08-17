/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.config;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.jpcode.solid.ComponentFactory;
import net.jpcode.solid.TaskSchedulerFactory;
import net.jpcode.solid.model.Content;
import net.jpcode.solid.schedule.DefaultTaskScheduler;
import net.jpcode.solid.spi.ContentFilter;
import net.jpcode.solid.spi.ContentGather;
import net.jpcode.solid.spi.ResumeParser;
import net.jpcode.solid.spi.Storage;
import net.jpcode.solid.spi.TextExtractor;

/**
 * @author billy zhang
 * 
 */
@Configuration
@ConfigurationProperties(prefix = "app.component")
public class ComponentConfig {

	private Map<String, String> contentGather;
	private Map<String, String> contentFilter;
	private Map<String, String> textExtractor;
	private Map<String, String> resumeParser;
	private Map<String, String> storage;
	
	@Autowired
	private AppConfig config;
	
	public void setContentGather(Map<String, String> contentGather) {
		this.contentGather = contentGather;
	}

	public void setContentFilter(Map<String, String> contentFilter) {
		this.contentFilter = contentFilter;
	}
	
	public void setTextExtractor(Map<String, String> textExtractor) {
		this.textExtractor = textExtractor;
	}
	
	public void setResumeParser(Map<String, String> resumeParser) {
		this.resumeParser = resumeParser;
	}
	
	public void setStorage(Map<String, String> storage) {
		this.storage = storage;
	}
	
	@Bean
	public TaskSchedulerFactory taskSchedulerFactory() {
		TaskSchedulerFactory factory = new TaskSchedulerFactory();
		factory.setScheduler(defaultTaskScheduler());
		
		return factory;
	}
	
	@Bean
	public DefaultTaskScheduler defaultTaskScheduler() {
		return new DefaultTaskScheduler();
	}
	
	@Bean
	public ComponentFactory<ContentGather> contentGatherFactory() throws Exception  {

		ComponentFactory<ContentGather> factory = new ComponentFactory<>();
		configComponentFactory( factory, contentGather );
		return factory;
	}
	
	@Bean
	public ComponentFactory<ContentFilter> contentFilterFactory() throws Exception  {

		ComponentFactory<ContentFilter> factory = new ComponentFactory<>();
		configComponentFactory( factory, contentFilter );
		return factory;
	}
	
	@Bean
	public ComponentFactory<TextExtractor> textExtractorFactory() throws Exception {
		
		TextExtractorFactory factory = new TextExtractorFactory();
		configComponentFactory( factory, textExtractor );
		return factory;
	}

	@Bean
	public ComponentFactory<ResumeParser> resumeParserFactory() throws Exception {

		ComponentFactory<ResumeParser> factory = new ComponentFactory<>();
		configComponentFactory( factory, resumeParser );
		return factory;
		
	}

	@Bean
	public ComponentFactory<Storage> storageFactory() throws Exception {

		ComponentFactory<Storage> factory = new ComponentFactory<>();
		configComponentFactory( factory, storage );
		return factory;
		
	}
	
	private <T> void configComponentFactory(ComponentFactory<T> factory, Map<String, String> map) 
			throws Exception {
		
		factory.setConfig(config.getConfig());
		
		for (Entry<String, String> entry : map.entrySet()) {
			Class<? extends T> clazz = (Class<? extends T>) Class.forName(entry.getValue());
			factory.register(entry.getKey(), clazz);
			
		}
		
	}
	
	private class TextExtractorFactory extends ComponentFactory<TextExtractor> {
		
		@Override
		public TextExtractor getDefault() {
			return new NopExtractor();
		}
		
		public class NopExtractor implements TextExtractor {

			@Override
			public String extract(Content content) throws Exception {
				return "";
			}
			
		}
	}
	
}
