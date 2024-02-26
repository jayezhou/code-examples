package com.tpzwl.be.api.model.res;

import java.io.Serializable;

public class Response<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long code;
	private String message;
	private T result;
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}

}
