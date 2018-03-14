package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.Permission;
import com.open.numberManagement.service.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PermissionService {
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}
	
	public Permission getPermissionByName(String name) {
		return this.permissionRepository.getPermissionByName(name);
	}
	
	public Permission addPermission(Permission permission) {
		return this.permissionRepository.save(permission);
	}
}
