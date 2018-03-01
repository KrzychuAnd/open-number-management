package com.open.numberManagement.service;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.ADD_RESULT_OK;
import static com.open.numberManagement.util.Constants.ADD_RESULT_NOK;
import static com.open.numberManagement.util.Constants.RESOURCE_EMPTY_STATUS_ID;
import static com.open.numberManagement.util.Constants.IS_VALID;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_RETIRED;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_LENGTH_INVALID;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_PREFIX_INVALID;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.dto.entity.ResourceResultDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceInvalidAgainstBusinessRulesException;
import com.open.numberManagement.exception.ResourceNotFoundException;
import com.open.numberManagement.exception.UserNoAccessToResourceException;
import com.open.numberManagement.exception.UserNoAccessToResourceTypeException;
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

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ResourceHistoryService resourceHistoryService;

	@Autowired
	private ResourceTypeService resourceTypeService;

	@Autowired
	private ResourceStatusService resourceStatusService;
	
	@Autowired
	private ResourceLifecycleService resourceLifecycleService;

	public ResourceService(ResourceRepository resourceRepository, ResourceHistoryService resourceHistoryService,
			ResourceTypeService resourceTypeService, ResourceLifecycleService resourceLifecycleService, ResourceStatusService resourceStatusService) {
		this.resourceRepository = resourceRepository;
		this.resourceHistoryService = resourceHistoryService;
		this.resourceTypeService = resourceTypeService;
		this.resourceStatusService = resourceStatusService;
		this.resourceLifecycleService = resourceLifecycleService;
	}

	public Resource getResourceById(Integer id) {
		Resource resource = this.resourceRepository.getResource(id).orElseThrow(() -> new ResourceNotFoundException(id));
		
		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceException(id);
		
		return resource;
	}

	public Resource getResourceByName(String name) {
		Resource resource = this.resourceRepository.getResourceByName(name).orElseThrow(() -> new ResourceNotFoundException(name));
		
		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceException(name);
		
		return resource;
	}

	public List<Resource> getResourcesByResTypeId(Integer resTypeId) {
		if (loggedUserHasNoAccessToResourceType(resTypeId))
			throw new UserNoAccessToResourceTypeException(resTypeId);
		
		return this.resourceRepository.getResourcesByResTypeId(resTypeId)
				.orElseThrow(() -> new ResourceNotFoundException(resTypeId, true));
	}

	public List<Resource> getResourcesByResTypeName(String resTypeName) {
		if (loggedUserHasNoAccessToResourceType(resTypeName))
			throw new UserNoAccessToResourceTypeException(resTypeName);
		
		return this.resourceRepository.getResourcesByResTypeName(resTypeName)
				.orElseThrow(() -> new ResourceNotFoundException(resTypeName, true));
	}

	public ResourcesDto addResources(ResourcesDto resourcesDto) {

		if (loggedUserHasNoAccessToResourceType(resourcesDto.getResTypeId()))
			throw new UserNoAccessToResourceTypeException(resourcesDto.getResTypeId());
			
		resourcesDto.getResources().forEach(new Consumer<ResourceResultDto>() {

			@Override
			public void accept(ResourceResultDto resourceDto) {
				Resource resource = new Resource();

				resource.setResTypeId(resourcesDto.getResTypeId());
				resource.setResStatusId(resourcesDto.getResStatusId());
				resource.setName(resourceDto.getName());
				resource.setDescr(resourceDto.getDescr());

				try {
					resource = addResource(resource);
					resourceDto.setId(resource.getId());
					resourceDto.setAddResult(ADD_RESULT_OK);
					resourceDto.setAddResultMessage("Resource added!");
				} catch (ResourceInvalidAgainstBusinessRulesException e) {
					resourceDto.setAddResult(ADD_RESULT_NOK);
					resourceDto.setAddResultMessage(e.getMessage());
				} catch (Exception e) {
					resourceDto.setAddResult(ADD_RESULT_NOK);
					resourceDto.setAddResultMessage(e.getMessage());
				}
			}
		});

		return resourcesDto;
	}

	@Transactional
	public Resource addResource(Resource resource) {
		ResourceHistory resourceHistory;

		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceTypeException(getResourceType(resource).getName());
		
		if (IS_VALID != isValidAgainstBusinessRules(resource, RESOURCE_EMPTY_STATUS_ID, resource.getResStatusId()))
			throw new ResourceInvalidAgainstBusinessRulesException(resource.getName());

		this.resourceRepository.save(resource);
		resourceHistory = new ResourceHistory(resource.getId(), RESOURCE_EMPTY_STATUS_ID, resource.getResStatusId());
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
	
	@Transactional
	public Resource retireResource(String resourceName) {
		Resource resource = getResourceByName(resourceName);
		Integer retiredStatusId = resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_RETIRED).getId();
		
		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceTypeException(getResourceType(resource).getName());
		
		if (IS_VALID != isValidAgainstBusinessRules(resource, resource.getResStatusId(), retiredStatusId))
			throw new ResourceInvalidAgainstBusinessRulesException(resource.getName());
		
		ResourceHistory resourceHistory = new ResourceHistory(resource.getId(), resource.getResStatusId(), retiredStatusId);
		this.resourceHistoryService.addResourceHistory(resourceHistory);
		resource.setResStatusId(retiredStatusId);
		
		this.resourceRepository.save(resource);
		return resource;
	}

	private boolean loggedUserHasNoAccessToResourceType(Resource resource) {
		ResourceType resourceType = this.getResourceType(resource);

		return hasNoAccessToResourceType(resourceType);
	}

	private boolean loggedUserHasNoAccessToResourceType(Integer resTypeId) {
		ResourceType resourceType = this.resourceTypeService.getResourceType(resTypeId);

		return hasNoAccessToResourceType(resourceType);
	}

	private boolean loggedUserHasNoAccessToResourceType(String resTypeName) {
		ResourceType resourceType = this.resourceTypeService.getResourceTypeByName(resTypeName);

		return hasNoAccessToResourceType(resourceType);
	}

	private boolean hasNoAccessToResourceType(ResourceType resourceType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return ((!(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMINISTRATOR_PERMISSION )))
				&& (Collections.disjoint(authentication.getAuthorities(), resourceType.getAuthorities())))
				&& !resourceType.getAuthorities().isEmpty());
	}

	private Integer isValidAgainstBusinessRules(Resource resource, Integer sourceStatusId, Integer targetStatusId) {
		ResourceType resourceType = this.getResourceType(resource);
		ResourceStatus targetResourceStatus = resourceStatusService.getResourceStatusById(targetStatusId);

		// Check if length of Resource.name equals ResourceType.length
		if (resource.getName().length() != resourceType.getLength()) {
			return ERR_RESOURCE_NAME_LENGTH_INVALID;
		}
		// Check if prefix of Resource.name equals ResourceType.prefix
		if (!resource.getName().startsWith("" + resourceType.getPrefix())) {
			return ERR_RESOURCE_NAME_PREFIX_INVALID;
		}
		// Check if Resource Status Transition is allowed
		List<ResourceStatus> targetResourceStatues = resourceLifecycleService.getTargetStatusesBySourceStatusId(sourceStatusId);
		if (targetResourceStatues == null | !targetResourceStatues.contains(targetResourceStatus)) {
			return ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED;
		}
		
		return IS_VALID;
	}

}
