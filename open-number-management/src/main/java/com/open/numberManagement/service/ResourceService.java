package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceNotFoundException;
import com.open.numberManagement.exception.UserNotFoundException;
import com.open.numberManagement.service.repository.ResourceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceService {
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Autowired
	private ResourceHistoryService resourceHistoryService;
	
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	public ResourceService(ResourceRepository resourceRepository, ResourceHistoryService resourceHistoryService, ResourceTypeService resourceTypeService) {
		this.resourceRepository = resourceRepository;
		this.resourceHistoryService = resourceHistoryService;
		this.resourceTypeService = resourceTypeService;
	}
	
	public Resource getResourceById(Integer id) {
		return this.resourceRepository.getResource(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public Resource getResourceByName(String name) {
		return this.resourceRepository.getResourceByName(name).orElseThrow(() -> new ResourceNotFoundException(name));
	}
	
	public List<Resource> getResourcesByResTypeId(Integer resTypeId){
		return this.resourceRepository.getResourcesByResTypeId(resTypeId).orElseThrow(() -> new ResourceNotFoundException(resTypeId, true));
	}
	
	public List<Resource> getResourcesByResTypeName(String resTypeName){
		return this.resourceRepository.getResourcesByResTypeName(resTypeName).orElseThrow(() -> new ResourceNotFoundException(resTypeName, true));
	}	
	
	public Resource addResource(Resource resource) {
		ResourceHistory resourceHistory;
		
		this.resourceRepository.save(resource);
		resourceHistory = new ResourceHistory(resource.getId(), null, resource.getResStatusId());
		this.resourceHistoryService.addResourceHistory(resourceHistory);
		return resource;
	}
	
	public void deleteResource(Resource resource) {
		this.resourceHistoryService.deleteResourceHistoryByResId(resource.getId());
		this.resourceRepository.delete(resource);
	}
	
	public ResourceType getResourceType(Resource resource) {
		return this.resourceTypeService.getResourceType(resource.getResTypeId());
	}

}
