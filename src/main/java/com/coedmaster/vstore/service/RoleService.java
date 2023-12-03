package com.coedmaster.vstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.service.contract.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role getRoleByName(String name) {
		return roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Role not found"));
	}

}
