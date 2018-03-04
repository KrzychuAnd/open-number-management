package com.open.numberManagement.controller;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.PageResourceDto;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.dto.entity.ResourceGenerateDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.service.ResourceTypeService;
import com.open.numberManagement.util.UriBuilder;

@RequestMapping(value = URL_VERSION_AND_RESOURCE_PATH)
@RestController
public class ResourceController {

	private ResourceService resourceService;
	private ResourceTypeService resourceTypeService;
	
	private DtoMapper dtoMapper;
	private UriBuilder uriBuilder = new UriBuilder();

	@Autowired
	public ResourceController(ResourceService resourceService, DtoMapper dtoMapper, ResourceTypeService resourceTypeService) {
		this.resourceService = resourceService;
		this.dtoMapper = dtoMapper;
		this.resourceTypeService = resourceTypeService;
	}

	@RequestMapping(value = "resTypeId/{resTypeId}", method = RequestMethod.GET)
	public PageResourceDto getResourcesByResTypeId(@PathVariable("resTypeId") Integer resTypeId,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		ResourceType resourceType = resourceTypeService.getResourceType(resTypeId);
		
		return resourceService.getResourcesByResTypeName(resourceType.getName(), pageNumber, pageSize);
	}
	
	@RequestMapping(value = "resTypeName/{resTypeName}", method = RequestMethod.GET)
	public PageResourceDto getResourcesByResTypeName(@PathVariable("resTypeName") String resTypeName,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		return resourceService.getResourcesByResTypeName(resTypeName, pageNumber, pageSize);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceDto getResource(@PathVariable("id") Integer id) {
		Resource resource = resourceService.getResourceById(id);

		ResourceDto resourceDto = dtoMapper.map(resource, ResourceDto.class);
		resourceDto.setHref(uriBuilder.getHrefWithId( URL_VERSION_AND_RESOURCE_PATH, resource.getId()));
		
		return resourceDto;
	}
	
	@RequestMapping(value = "name/{name}", method = RequestMethod.GET)
	@ResponseBody
	public ResourceDto getResource(@PathVariable("name") String name) {
		Resource resource = resourceService.getResourceByName(name);

		ResourceDto resourceDto = dtoMapper.map(resource, ResourceDto.class);
		resourceDto.setHref(uriBuilder.getHrefWithId( URL_VERSION_AND_RESOURCE_PATH, resource.getId()));
		
		return resourceDto;
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
	
	@PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResourceDto patchResource(@PathVariable("id") Integer id, @RequestBody Map<String, Object> updates) {
		return resourceService.patchResource(id, updates);
	}

	@RequestMapping(value= "many", method = RequestMethod.POST)
	@ResponseBody
	public ResourcesDto addResources(@RequestBody ResourcesDto resourcesDto) {
		
		resourcesDto = resourceService.addResources(resourcesDto);
		
		return resourcesDto;
	}
	
	@RequestMapping(value = "retire/{name}", method = RequestMethod.PATCH)
	public ResponseEntity<Resource> retireResource(@PathVariable("name") String name) {
		Resource resource = resourceService.retireResource(name);

		return noContent().build();
	}
	
	@RequestMapping(value= "generate", method = RequestMethod.POST)
	@ResponseBody
	public ResourcesDto generateResources(@RequestBody ResourceGenerateDto resourceGenerateDto) {
		
		ResourcesDto resourcesDto = resourceService.generateResources(resourceGenerateDto);
		
		return resourcesDto;
	}	
	
	@RequestMapping(value= "reserve", method = RequestMethod.PATCH)
	@ResponseBody
	public List<ResourceDto> reserveResources(@RequestBody ResourceGenerateDto resourceGenerateDto) {
		
		return resourceService.reserveResources(resourceGenerateDto);
	}	
}
