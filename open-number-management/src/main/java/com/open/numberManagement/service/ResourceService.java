package com.open.numberManagement.service;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.RESOURCE_RESULT_OK;
import static com.open.numberManagement.util.Constants.RESOURCE_RESULT_NOK;
import static com.open.numberManagement.util.Constants.RESOURCE_EMPTY_STATUS_ID;
import static com.open.numberManagement.util.Constants.RESOURCE_RESULT_GENERATE_OK_WITH_WARNING;
import static com.open.numberManagement.util.Constants.RESOURCE_GENERATE_RESULT_MSG;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_RETIRED;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_RESERVED;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_LENGTH_INVALID;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_PREFIX_INVALID;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED;
import static com.open.numberManagement.util.Constants.INFO_RESOURCE_AUTO_GENERATED;
import static com.open.numberManagement.util.Constants.INFO_RESOURCE_ADDED_WITH_SUCCESS;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;
import static com.open.numberManagement.util.Constants.NUMBER_OF_RESOURCES_TO_GENERATE_LIMIT;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_LENGTH_INVALID_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NAME_PREFIX_INVALID_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS_MSG;
import static com.open.numberManagement.util.Constants.NULL_STRING;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_PATCH_JSON_PARAMETER_NOT_EXISTS;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_AND_RELATED_RESOURCE_EQUALS;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_AND_RELATED_RESOURCE_EQUALS_MSG;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.PageDto;
import com.open.numberManagement.dto.entity.PageResourceDto;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.dto.entity.ResourceGenerateDto;
import com.open.numberManagement.dto.entity.ResourceResultDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceGenerateLimitNumberException;
import com.open.numberManagement.exception.ResourceInvalidAgainstBusinessRulesException;
import com.open.numberManagement.exception.ResourceNotFoundException;
import com.open.numberManagement.exception.UserNoAccessToResourceException;
import com.open.numberManagement.exception.UserNoAccessToResourceTypeException;
import com.open.numberManagement.service.repository.ResourceRepository;
import com.open.numberManagement.util.UriBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

	@Autowired
	private DtoMapper dtoMapper;

	private UriBuilder uriBuilder = new UriBuilder();

	public ResourceService(ResourceRepository resourceRepository, ResourceHistoryService resourceHistoryService,
			ResourceTypeService resourceTypeService, ResourceLifecycleService resourceLifecycleService,
			ResourceStatusService resourceStatusService) {
		this.resourceRepository = resourceRepository;
		this.resourceHistoryService = resourceHistoryService;
		this.resourceTypeService = resourceTypeService;
		this.resourceStatusService = resourceStatusService;
		this.resourceLifecycleService = resourceLifecycleService;
	}

	public Resource getResourceById(Integer id) {
		Resource resource = this.resourceRepository.getResource(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));

		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceException(id);

		return resource;
	}

	public Resource getResourceByName(String name) {
		Resource resource = this.resourceRepository.getResourceByName(name)
				.orElseThrow(() -> new ResourceNotFoundException(name));

		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceException(name);

		return resource;
	}

	public PageResourceDto getResourcesByResTypeName(String resTypeName, int pageNumber, int pageSize) {
		if (loggedUserHasNoAccessToResourceType(resTypeName))
			throw new UserNoAccessToResourceTypeException(resTypeName);

		Page<Resource> resources = this.resourceRepository
				.getResourcesByResTypeName(resTypeName,
						new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "name"))
				.orElseThrow(() -> new ResourceNotFoundException(resTypeName, true));

		List<ResourceDto> resourceDtos = dtoMapper.map(resources.getContent(), ResourceDto.class);

		resourceDtos.forEach(new Consumer<ResourceDto>() {

			@Override
			public void accept(ResourceDto resourceDto) {
				resourceDto.setHref(uriBuilder.getHrefWithId(URL_VERSION_AND_RESOURCE_PATH, resourceDto.getId()));
			}
		});

		PageResourceDto pageResourceDto = new PageResourceDto();
		pageResourceDto.setResources(resourceDtos);
		pageResourceDto.setPage(new PageDto(resources.getSize(), resources.getTotalElements(),
				resources.getTotalPages(), resources.getNumber(), resources.getNumberOfElements()));

		return pageResourceDto;
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
					resourceDto.setAddResult(RESOURCE_RESULT_OK);
					resourceDto.setAddResultMessage(INFO_RESOURCE_ADDED_WITH_SUCCESS);
					resourceDto.setHref(uriBuilder.getHrefWithId(URL_VERSION_AND_RESOURCE_PATH, resourceDto.getId()));
				} catch (ResourceInvalidAgainstBusinessRulesException e) {
					resourceDto.setAddResult(RESOURCE_RESULT_NOK);
					resourceDto.setAddResultMessage(e.getMessage());
				} catch (Exception e) {
					resourceDto.setAddResult(RESOURCE_RESULT_NOK);
					resourceDto.setAddResultMessage(e.getMessage());
				}
			}
		});

		return resourcesDto;
	}

	@Transactional
	public ResourcesDto generateResources(ResourceGenerateDto resourceGenerateDto) {
		Integer number = resourceGenerateDto.getNumber();
		ResourcesDto resourcesDto = new ResourcesDto();
		ResourceType resourceType = resourceTypeService.getResourceType(resourceGenerateDto.getResTypeId());
		Long currentMaxResourceNumber = this.getMaxResourceNumberByResTypeId(resourceGenerateDto.getResTypeId());
		String name, maxname;
		Long resourceNumber, maxResourceNumber;
		Integer counter = 1;

		resourcesDto.setResTypeId(resourceGenerateDto.getResTypeId());
		resourcesDto.setResStatusId(resourceGenerateDto.getResStatusId());
		resourcesDto.setResources(new ArrayList<ResourceResultDto>());

		if (number > NUMBER_OF_RESOURCES_TO_GENERATE_LIMIT)
			throw new ResourceGenerateLimitNumberException();

		if (loggedUserHasNoAccessToResourceType(resourcesDto.getResTypeId()))
			throw new UserNoAccessToResourceTypeException(resourcesDto.getResTypeId());

		isAllowedStatusTransition(RESOURCE_EMPTY_STATUS_ID, resourcesDto.getResStatusId());

		maxname = resourceType.getPrefix() + StringUtils.leftPad("9",
				(resourceType.getLength() - String.valueOf(resourceType.getPrefix()).length()), "9");

		maxResourceNumber = Long.parseLong(maxname, 10);

		if (currentMaxResourceNumber == 0) {
			// First number of this Resource Type
			name = resourceType.getPrefix() + StringUtils.leftPad("1",
					(resourceType.getLength() - String.valueOf(resourceType.getPrefix()).length()), "0");
			resourceNumber = Long.parseLong(name, 10);
		} else {
			resourceNumber = currentMaxResourceNumber + 1;
		}

		if (resourceNumber > maxResourceNumber) {
			throw new ResourceInvalidAgainstBusinessRulesException(ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS,
					String.format(ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS_MSG, resourceType.getName()));
		}

		for (counter = 0; counter < number & resourceNumber <= maxResourceNumber; counter++, resourceNumber++) {
			Resource resource = new Resource();
			ResourceHistory resourceHistory;

			resource.setResStatusId(resourceGenerateDto.getResStatusId());
			resource.setResTypeId(resourceGenerateDto.getResTypeId());
			resource.setDescr(INFO_RESOURCE_AUTO_GENERATED);

			resource.setName(Long.toString(resourceNumber));

			this.resourceRepository.save(resource);
			resourceHistory = new ResourceHistory(resource.getId(), RESOURCE_EMPTY_STATUS_ID,
					resource.getResStatusId());
			this.resourceHistoryService.addResourceHistory(resourceHistory);

			ResourceResultDto resourceResultDto = dtoMapper.map(resource, ResourceResultDto.class);
			resourceResultDto.setAddResultMessage(INFO_RESOURCE_ADDED_WITH_SUCCESS);
			resourceResultDto
					.setHref(uriBuilder.getHrefWithId(URL_VERSION_AND_RESOURCE_PATH, resourceResultDto.getId()));
			resourcesDto.getResources().add(resourceResultDto);
		}

		if (counter != number) {
			resourcesDto.getResult().setCode(RESOURCE_RESULT_GENERATE_OK_WITH_WARNING);
			resourcesDto.getResult().setMessage(String.format(RESOURCE_GENERATE_RESULT_MSG, counter, number));
		}

		return resourcesDto;
	}

	@Transactional
	public Resource addResource(Resource resource) {
		ResourceHistory resourceHistory;

		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceTypeException(getResourceType(resource).getName());

		isValidAgainstBusinessRules(resource, RESOURCE_EMPTY_STATUS_ID, resource.getResStatusId());

		this.resourceRepository.save(resource);
		resourceHistory = new ResourceHistory(resource.getId(), RESOURCE_EMPTY_STATUS_ID, resource.getResStatusId(),
				null, resource.getRelResId());
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

		isValidAgainstBusinessRules(resource, resource.getResStatusId(), retiredStatusId);

		ResourceHistory resourceHistory = new ResourceHistory(resource.getId(), resource.getResStatusId(),
				retiredStatusId);
		this.resourceHistoryService.addResourceHistory(resourceHistory);
		resource.setResStatusId(retiredStatusId);

		this.resourceRepository.save(resource);
		return resource;
	}

	@Transactional
	public ResourceDto patchResource(Integer id, Map<String, Object> updates) {
		Resource resource = this.getResourceById(id);
		ResourceHistory resourceHistory = new ResourceHistory();

		if (loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceTypeException(getResourceType(resource).getName());

		resourceHistory.setResId(id);
		resourceHistory.setSourceStatusId(resource.getResStatusId());
		resourceHistory.setTargetStatusId(resource.getResStatusId());
		resourceHistory.setOldRelResId(resource.getRelResId());
		resourceHistory.setNewRelResId(resource.getRelResId());

		for (Map.Entry<String, Object> entry : updates.entrySet()) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			Integer iValue = null;

			switch (key) {
			case "relResId":
				if (!value.equals(NULL_STRING)) {
					iValue = Integer.valueOf(value);
					Resource relResource = this.getResourceById(iValue);
					
					if (iValue.equals(resource.getId()))
						throw new ResourceInvalidAgainstBusinessRulesException(ERR_RESOURCE_AND_RELATED_RESOURCE_EQUALS,
								String.format(ERR_RESOURCE_AND_RELATED_RESOURCE_EQUALS_MSG));
				}
				resource.setRelResId(iValue);
				resourceHistory.setNewRelResId(iValue);
				break;
			case "resStatusId":
				if (!value.equals(NULL_STRING)) {
					iValue = Integer.valueOf(value);
				}
				isAllowedStatusTransition(resource.getResStatusId(), iValue);

				resource.setResStatusId(iValue);
				resourceHistory.setTargetStatusId(iValue);
				break;
			case "descr":
				resource.setDescr(value);
				break;
			default:
				try {
					resource.getClass().getField(key);
				} catch (NoSuchFieldException e) {
					throw new HttpMessageNotReadableException(
							String.format(ERR_RESOURCE_PATCH_JSON_PARAMETER_NOT_EXISTS, key));
				}
			}
		}

		resourceHistoryService.addResourceHistory(resourceHistory);
		resourceRepository.save(resource);

		ResourceDto resourceDto = dtoMapper.map(resource, ResourceDto.class);
		resourceDto.setHref(uriBuilder.getHrefWithId(URL_VERSION_AND_RESOURCE_PATH, resourceDto.getId()));
		return resourceDto;
	}

	@Transactional
	public List<ResourceDto> reserveResources(ResourceGenerateDto resourceGenerateDto) {
		ResourceStatus availableResourceStatus = resourceStatusService
				.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);
		ResourceStatus reservedResourceStatus = resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_RESERVED);

		if (loggedUserHasNoAccessToResourceType(resourceGenerateDto.getResTypeId()))
			throw new UserNoAccessToResourceTypeException(resourceGenerateDto.getResTypeId());

		isAllowedStatusTransition(availableResourceStatus.getId(), reservedResourceStatus.getId());

		List<Resource> resources = resourceRepository
				.getResourcesByResTypeAndStatusId(resourceGenerateDto.getResTypeId(), availableResourceStatus.getId(),
						new PageRequest(0, resourceGenerateDto.getNumber(), Sort.Direction.ASC, "name"))
				.orElseThrow(() -> new ResourceNotFoundException(resourceGenerateDto.getResTypeId(),
						availableResourceStatus.getId()));

		resources.forEach(new Consumer<Resource>() {

			@Override
			public void accept(Resource resource) {
				ResourceHistory resourceHistory = new ResourceHistory(resource.getId(), resource.getResStatusId(),
						reservedResourceStatus.getId());
				resourceHistoryService.addResourceHistory(resourceHistory);
				resource.setResStatusId(reservedResourceStatus.getId());
				resourceRepository.save(resource);
			}

		});

		List<ResourceDto> resourcesDto = dtoMapper.map(resources, ResourceDto.class);

		resourcesDto.forEach(new Consumer<ResourceDto>() {

			@Override
			public void accept(ResourceDto resourceDto) {
				resourceDto.setHref(uriBuilder.getHrefWithId(URL_VERSION_AND_RESOURCE_PATH, resourceDto.getId()));

			}

		});

		return resourcesDto;
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

		return ((!(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMINISTRATOR_PERMISSION)))
				&& (Collections.disjoint(authentication.getAuthorities(), resourceType.getAuthorities())))
				&& !resourceType.getAuthorities().isEmpty());
	}

	private void isValidAgainstBusinessRules(Resource resource, Integer sourceStatusId, Integer targetStatusId) {

		checkResourceNameLength(resource);

		checkResourceNamePrefix(resource);

		isAllowedStatusTransition(sourceStatusId, targetStatusId);
	}

	// Check if length of Resource.name equals ResourceType.length
	private void checkResourceNameLength(Resource resource) {
		ResourceType resourceType = this.getResourceType(resource);

		if (resource.getName().length() != resourceType.getLength()) {
			// return ERR_RESOURCE_NAME_LENGTH_INVALID;
			throw new ResourceInvalidAgainstBusinessRulesException(ERR_RESOURCE_NAME_LENGTH_INVALID, String
					.format(ERR_RESOURCE_NAME_LENGTH_INVALID_MSG, resourceType.getName(), resourceType.getLength()));
		}
	}

	// Check if prefix of Resource.name equals ResourceType.prefix
	private void checkResourceNamePrefix(Resource resource) {
		ResourceType resourceType = this.getResourceType(resource);

		if (!resource.getName().startsWith("" + resourceType.getPrefix())) {
			throw new ResourceInvalidAgainstBusinessRulesException(ERR_RESOURCE_NAME_PREFIX_INVALID, String
					.format(ERR_RESOURCE_NAME_PREFIX_INVALID_MSG, resourceType.getName(), resourceType.getPrefix()));
		}
	}

	// Check if Resource Status Transition is allowed
	private void isAllowedStatusTransition(Integer sourceStatusId, Integer targetStatusId) {

		ResourceStatus sourceResourceStatus = resourceStatusService.getResourceStatusById(sourceStatusId);
		ResourceStatus targetResourceStatus = resourceStatusService.getResourceStatusById(targetStatusId);

		if (sourceStatusId == targetStatusId)
			return;

		List<ResourceStatus> targetResourceStatues = resourceLifecycleService
				.getTargetStatusesBySourceStatusId(sourceStatusId);
		if (targetResourceStatues == null | !targetResourceStatues.contains(targetResourceStatus)) {
			throw new ResourceInvalidAgainstBusinessRulesException(ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED,
					String.format(ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED_MSG, sourceResourceStatus.getName(),
							targetResourceStatus.getName()));
		}

	}

	private Long getMaxResourceNumberByResTypeId(Integer resTypeId) {
		return resourceRepository.getMaxResourceNumberByResTypeId(resTypeId).orElse((long) 0);
	}

}
