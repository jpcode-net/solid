/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: billy zhang
 */
@Slf4j
@ControllerAdvice
public class WebErrorHandler {
	  
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
    public JsonResult<Integer> methodNotSupportedHandler(
    		HttpServletRequest req, Exception ex) {

		log.error(ex.getMessage() + ", " + req.getRequestURL().toString(), ex);
		
		return JsonResult.fail(-1, "请求方法不支持。");
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
    public JsonResult<Integer> messageNotReadableHandler(
    		HttpServletRequest req, Exception ex) {

		log.error(ex.getMessage() + ", " + req.getRequestURL().toString(), ex);
		
		return JsonResult.fail(-1, "缺少请求参数或参数类型不符。");
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
    public JsonResult<Integer> noHandlerFoundHandler(
    		HttpServletRequest req, Exception ex) {

		log.error(ex.getMessage() + ", " + req.getRequestURL().toString(), ex);
		
		return JsonResult.fail(-1, "请求不存在。");
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public JsonResult<Integer> defaultHandler(HttpServletRequest req, Exception ex) {

		log.error(ex.getMessage() + ", " + req.getRequestURL().toString(), ex);
		
		return JsonResult.fail(-1, ex.getMessage());
	}

}
