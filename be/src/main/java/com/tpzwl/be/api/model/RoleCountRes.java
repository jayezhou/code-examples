package com.tpzwl.be.api.model;

import java.util.List;

public class RoleCountRes {
	
	private Long code;
	private List<RoleCount> result;
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public List<RoleCount> getResult() {
		return result;
	}
	public void setResult(List<RoleCount> result) {
		this.result = result;
	}

}
