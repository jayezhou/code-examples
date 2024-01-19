package com.tpzwl.be.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpzwl.be.api.model.RoleCount;
import com.tpzwl.be.api.model.User;
import com.tpzwl.be.api.repository.UserRepository;
import com.tpzwl.be.api.security.repository.RoleRepository;

@Service
public class ExampleService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public List<RoleCount> roleCount() {
		return roleRepository.countByRole();
	}

}
