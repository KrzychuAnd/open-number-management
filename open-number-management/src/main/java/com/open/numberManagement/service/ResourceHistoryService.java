package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.service.repository.ResourceHistoryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceHistoryService {
	
	@Autowired
	private ResourceHistoryRepository resourceHistoryRepository;
	
	public ResourceHistoryService(ResourceHistoryRepository resourceHistoryRepository) {
		this.resourceHistoryRepository = resourceHistoryRepository;
	}

	public ResourceHistory addResourceHistory(ResourceHistory resourceHistory) {
		return this.resourceHistoryRepository.save(resourceHistory);
	}
	
	public void deleteResourceHistoryByResId(Integer resourceId) {
		this.resourceHistoryRepository.delete(this.resourceHistoryRepository.getResourceHistoriesByResId(resourceId));
	}
	
	public List<ResourceHistory> getResourceHistory(Integer resourceId){
		return this.resourceHistoryRepository.getResourceHistoriesByResId(resourceId);
	}
}
