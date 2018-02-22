package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceTypeNotFoundException;
import com.open.numberManagement.service.repository.ResourceTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceTypeService {
	
	@Autowired
	private ResourceTypeRepository resourceRepository;
	
	public ResourceTypeService(ResourceTypeRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}

	public ResourceType addResourceType(ResourceType resourceType) {
		return this.resourceRepository.save(resourceType);
	}
	
	public void deleteResourceType(ResourceType resourceType) {
		this.resourceRepository.delete(resourceType);
	}
	
	public ResourceType getResourceType(Integer id) {
		return this.resourceRepository.getResourceType(id).orElseThrow(() -> new ResourceTypeNotFoundException(id));
	}
	
	public ResourceType getResourceTypeByName(String name) {
		return this.resourceRepository.getResourceTypeByName(name).orElseThrow(() -> new ResourceTypeNotFoundException(name));
	}
}
