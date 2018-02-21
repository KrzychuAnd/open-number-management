package com.open.numberManagement.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.UserNoAccessToResourceException;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.util.UriBuilder;

@RequestMapping(value = "v1/resources")
@RestController
public class ResourceController {

	private ResourceService resourceService;
	private DtoMapper dtoMapper;
	private UriBuilder uriBuilder = new UriBuilder();

	@Autowired
	public ResourceController(ResourceService resourceService, DtoMapper dtoMapper) {
		this.resourceService = resourceService;
		this.dtoMapper = dtoMapper;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceDto getResource(@PathVariable("id") Integer id) {
		Resource resource = resourceService.getResourceById(id);

		if (loggedUserHasNoAccessToResource(resource))
			throw new UserNoAccessToResourceException(id);

		return dtoMapper.map(resource, ResourceDto.class);
	}

	private boolean loggedUserHasNoAccessToResource(Resource resource) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ResourceType resourceType = resourceService.getResourceType(resource);
		
		return ((!(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN_PERM")))
				&& (Collections.disjoint(authentication.getAuthorities(), resourceType.getAuthorities())))
				&& !resourceType.getAuthorities().isEmpty());
	}
}
