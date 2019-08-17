/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid;

/**
 * @author: billy zhang
 */
public class JsonResult<T> {

	public static <T> JsonResult<T> ok(T data) {
		JsonResult<T> jsonRet = new JsonResult<>();
		jsonRet.setData(data);
		
		return jsonRet;
	}
	
	public static JsonResult fail(int code, String msg) {
		JsonResult jsonRet = new JsonResult();
		jsonRet.setErrCode(code);
		jsonRet.setErrMsg(msg);
		
		return jsonRet;
	}
	
	private int errCode;
	private String errMsg;
	private T data;
	
	private JsonResult() {
		
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
