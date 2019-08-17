/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.jpcode.solid.JsonResult;
import net.jpcode.solid.util.CryptoUtils;

/**
 * @author: billy zhang
 */
public class ApiInterceptor implements HandlerInterceptor {

	private static final String SECRET_KEY = "abcdef123456";
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		response.setContentType("application/json; charset=utf-8");
		
		String ts = request.getParameter("ts");
		String sign = request.getParameter("sign");
		if (StringUtils.isEmpty(ts) || StringUtils.isEmpty(sign)) {
			JsonResult<?> jsonRet = JsonResult.fail(-1, "无效的参数。");
			response.getWriter().write(jsonMapper.writeValueAsString(jsonRet));
			return false;
		}
		
		if (!verifySign(ts, sign)) {
			JsonResult<?> jsonRet = JsonResult.fail(-1, "签名不正确。");
			response.getWriter().write(jsonMapper.writeValueAsString(jsonRet));
			return false;
		}
		
		return true;
	}

	private boolean verifySign(String ts, String sign) {
		String planStr = String.format("ts=%s&secret=%s", ts, SECRET_KEY);
		return sign.equalsIgnoreCase(CryptoUtils.MD5(planStr));
	}
	
}
