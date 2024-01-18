package com.tpzwl.be.api.payload.request;

import com.tpzwl.be.api.security.model.EnumDeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
	
	@NotNull
	private EnumDeviceType deviceType;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public EnumDeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(EnumDeviceType deviceType) {
		this.deviceType = deviceType;
	}

}
