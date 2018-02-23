package com.open.numberManagement.service;

import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.service.repository.ResourceLifecycleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceLifecycleService {
	
	@Autowired
	private ResourceLifecycleRepository resourceLifecycleRepository;
	
	public ResourceLifecycleService(ResourceLifecycleRepository resourceLifecycleRepository) {
		this.resourceLifecycleRepository = resourceLifecycleRepository;
	}
	
	public List<ResourceStatus> getTargetStatusesBySourceStatusId(Integer sourceStatusId){
		return this.resourceLifecycleRepository.getTargetStatusesBySourceStatusId(sourceStatusId); 
	}
}
