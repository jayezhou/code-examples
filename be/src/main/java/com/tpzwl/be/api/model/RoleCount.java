package com.tpzwl.be.api.model;

import com.tpzwl.be.api.security.model.EnumRole;

public class RoleCount {
	
	private Long id;
	private EnumRole name;
	private Long count;

	public RoleCount(Long id, String name, Long count) {
		this.id = id;
		this.name = EnumRole.valueOf(name);
		this.count = count;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public EnumRole getName() {
		return name;
	}

	public void setName(EnumRole name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
