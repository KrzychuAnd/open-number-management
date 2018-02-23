package com.open.numberManagement.service;

import static com.open.numberManagement.dto.entity.ResourceResultDto.ADD_RESULT_OK;
import static com.open.numberManagement.dto.entity.ResourceResultDto.ADD_RESULT_NOK;
import static com.open.numberManagement.entity.ResourceHistory.EMPTY_STATUS;
import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.dto.entity.ResourceResultDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceNotFoundException;
import com.open.numberManagement.service.repository.ResourceRepository;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
	
	public static final Integer IS_VALID = 0;
	public static final Integer ERR_RESOURCE_NAME_LENGTH_INVALID = 100;
	public static final Integer ERR_RESOURCE_NAME_PREFIX_INVALID = 200;
	
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
	
	public ResourcesDto addResources(ResourcesDto resourcesDto){
		
		resourcesDto.getResources().forEach(new Consumer<ResourceResultDto>() {

			@Override
			public void accept(ResourceResultDto resourceDto) {
				Resource resource = new Resource();
				
				resource.setResTypeId(resourcesDto.getResTypeId());
				resource.setResStatusId(resourcesDto.getResStatusId());
				resource.setName(resourceDto.getName());
				resource.setDescr(resourceDto.getDescr());
				
				if (IS_VALID != isValidAgainstResourceTypeDef(resource)) {
					resourceDto.setAddResult(ADD_RESULT_NOK);
					resourceDto.setAddResultMessage("Resource name \'" + resource.getName() + "\' is not valid against Resource Type definition.");
				}else {
					try {
						resource = addResource(resource);
						resourceDto.setId(resource.getId());
						resourceDto.setAddResult(ADD_RESULT_OK);
						resourceDto.setAddResultMessage("Resource added!");
					}catch(Exception e) {
						resourceDto.setAddResult(ADD_RESULT_NOK);
						resourceDto.setAddResultMessage(e.getMessage());						
					}
				}
			}
		});
		
		return resourcesDto;
	}
	
	@Transactional
	public Resource addResource(Resource resource) {
		ResourceHistory resourceHistory;
		
		this.resourceRepository.save(resource);
		resourceHistory = new ResourceHistory(resource.getId(), EMPTY_STATUS, resource.getResStatusId());
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
	
	public boolean loggedUserHasNoAccessToResourceType(Resource resource) {
		ResourceType resourceType = this.getResourceType(resource);
		
		return hasNoAccessToResourceType(resourceType);
	}
	
	public boolean loggedUserHasNoAccessToResourceType(Integer resTypeId) {
		ResourceType resourceType = this.resourceTypeService.getResourceType(resTypeId);
		
		return hasNoAccessToResourceType(resourceType);
	}	
	
	public boolean loggedUserHasNoAccessToResourceType(String resTypeName) {
		ResourceType resourceType = this.resourceTypeService.getResourceTypeByName(resTypeName);
		
		return hasNoAccessToResourceType(resourceType);
	}		
	
	private boolean hasNoAccessToResourceType(ResourceType resourceType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return ((!(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN_PERM")))
				&& (Collections.disjoint(authentication.getAuthorities(), resourceType.getAuthorities())))
				&& !resourceType.getAuthorities().isEmpty());
	}
	
	public Integer isValidAgainstResourceTypeDef(Resource resource) {
		ResourceType resourceType = this.getResourceType(resource);
		
		//Check if length of Resource.name equals ResourceType.length 
		if(resource.getName().length() != resourceType.getLength()) {
			return ERR_RESOURCE_NAME_LENGTH_INVALID;
		}
		//Check if prefix of Resource.name equals ResourceType.prefix
		if(!resource.getName().startsWith("" + resourceType.getPrefix())) {
			return ERR_RESOURCE_NAME_PREFIX_INVALID;
		}		
		return IS_VALID;
	}

}
