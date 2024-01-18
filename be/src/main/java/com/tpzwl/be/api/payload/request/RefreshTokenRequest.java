package com.tpzwl.be.api.payload.request;

import com.tpzwl.be.api.security.model.EnumDeviceType;

import jakarta.validation.constraints.NotNull;

public class RefreshTokenRequest {

	@NotNull
	private EnumDeviceType deviceType;
	
	public EnumDeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(EnumDeviceType deviceType) {
		this.deviceType = deviceType;
	}

}
