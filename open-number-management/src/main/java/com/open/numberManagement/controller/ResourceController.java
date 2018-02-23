package com.open.numberManagement.controller;

import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
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

	@RequestMapping(value = "resTypeId/{resTypeId}", method = RequestMethod.GET)
	public List<ResourceDto> getResourcesByResTypeId(@PathVariable("resTypeId") Integer resTypeId,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		List<Resource> resources = resourceService.getResourcesByResTypeId(resTypeId);
		List<ResourceDto> resourceDtos = dtoMapper.map(resources, ResourceDto.class);
		return resourceDtos;
	}
	
	@RequestMapping(value = "resTypeName/{resTypeName}", method = RequestMethod.GET)
	public List<ResourceDto> getResourcesByResTypeName(@PathVariable("resTypeName") String resTypeName,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		List<Resource> resources = resourceService.getResourcesByResTypeName(resTypeName);
		List<ResourceDto> resourceDtos = dtoMapper.map(resources, ResourceDto.class);
		return resourceDtos;
	}
	
	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceDto getResource(@PathVariable("id") Integer id) {
		Resource resource = resourceService.getResourceById(id);

		return dtoMapper.map(resource, ResourceDto.class);
	}
	
	@RequestMapping(value = "name/{name}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceDto getResource(@PathVariable("name") String name) {
		Resource resource = resourceService.getResourceByName(name);

		return dtoMapper.map(resource, ResourceDto.class);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Resource> addResource(@RequestBody ResourceDto resourceDto) {
		Resource resource = new Resource();
		URI uri;
		
		dtoMapper.map(resourceDto, resource);

		resource = resourceService.addResource(resource);
		uri = uriBuilder.requestUriWithId(resource.getId());

		return created(uri).build();
	}

	@RequestMapping(value= "many", method = RequestMethod.POST)
	@ResponseBody
	public ResourcesDto addResources(@RequestBody ResourcesDto resourcesDto) {
		
		resourcesDto = resourceService.addResources(resourcesDto);
		
		return resourcesDto;
	}
}
