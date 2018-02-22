package com.open.numberManagement.controller;

import static org.springframework.http.ResponseEntity.created;
import static com.open.numberManagement.service.ResourceService.IS_VALID;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.User;
import com.open.numberManagement.exception.ResourceInvalidAgainstResourceTypeException;
import com.open.numberManagement.exception.UserNoAccessToResourceException;
import com.open.numberManagement.exception.UserNoAccessToResourceTypeException;
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

		if (resourceService.loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceException(id);

		return dtoMapper.map(resource, ResourceDto.class);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody ResourceDto resourceDto) {
		Resource resource = new Resource();
		URI uri;
		
		dtoMapper.map(resourceDto, resource);
		
		if (resourceService.loggedUserHasNoAccessToResourceType(resource))
			throw new UserNoAccessToResourceTypeException(resourceService.getResourceType(resource).getName());
		
		if (IS_VALID != resourceService.isValidAgainstResourceTypeDef(resource))
			throw new ResourceInvalidAgainstResourceTypeException(resource.getName());

		resource = resourceService.addResource(resource);
		uri = uriBuilder.requestUriWithId(resource.getId());

		return created(uri).build();
	}

}
