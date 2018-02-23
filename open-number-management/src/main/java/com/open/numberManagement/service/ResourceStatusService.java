package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.exception.ResourceStatusNotFoundException;
import com.open.numberManagement.service.repository.ResourceStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceStatusService {
	
	@Autowired
	private ResourceStatusRepository resourceStatusRepository;
	
	public ResourceStatusService(ResourceStatusRepository resourceStatusRepository) {
		this.resourceStatusRepository = resourceStatusRepository;
	}

	public ResourceStatus addResourceType(ResourceStatus resourceStatus) {
		return this.resourceStatusRepository.save(resourceStatus);
	}
	
	public void deleteResourceStatus(ResourceStatus resourceStatus) {
		this.resourceStatusRepository.delete(resourceStatus);
	}
	
	public ResourceStatus getResourceStatusById(Integer id){
		return this.resourceStatusRepository.getResourceStatusById(id).orElseThrow(() -> new ResourceStatusNotFoundException(id));
	}
	
	public ResourceStatus getResourceStatusByName(String name) {
		return this.resourceStatusRepository.getResourceStatusByName(name);
	}
}
