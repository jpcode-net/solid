/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.jpcode.solid.interceptor.ApiInterceptor;

/**
 * @author: billy zhang
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer { 

	@Bean
	public ApiInterceptor apiInterceptor() {
		return new ApiInterceptor();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    
    }
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    	 registry.addInterceptor(apiInterceptor())   
			.addPathPatterns("/api/**");

       
    }

}
