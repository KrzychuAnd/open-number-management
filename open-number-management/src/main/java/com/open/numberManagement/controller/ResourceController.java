package com.open.numberManagement.controller;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.PageResourceDto;
import com.open.numberManagement.dto.entity.ResourceCountDto;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.dto.entity.ResourceGenerateDto;
import com.open.numberManagement.dto.entity.ResourceTypeDto;
import com.open.numberManagement.dto.entity.ResourcesDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceCount;
import com.open.numberManagement.entity.ResourceHistory;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.service.ResourceHistoryService;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.service.ResourceTypeService;
import com.open.numberManagement.util.UriBuilder;

@RequestMapping(value = URL_VERSION_AND_RESOURCE_PATH)
@RestController
public class ResourceController {

	private ResourceService resourceService;
	private ResourceTypeService resourceTypeService;
	private ResourceHistoryService resourceHistoryService;
	
	private DtoMapper dtoMapper;
	private UriBuilder uriBuilder = new UriBuilder();

	@Autowired
	public ResourceController(ResourceService resourceService, DtoMapper dtoMapper, ResourceTypeService resourceTypeService, ResourceHistoryService resourceHistoryService) {
		this.resourceService = resourceService;
		this.dtoMapper = dtoMapper;
		this.resourceTypeService = resourceTypeService;
		this.resourceHistoryService = resourceHistoryService;
	}

	@GetMapping(value = "resTypeId/{resTypeId}")
	public PageResourceDto getResourcesByResTypeId(@PathVariable("resTypeId") Integer resTypeId,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		ResourceType resourceType = resourceTypeService.getResourceType(resTypeId);
		
		return resourceService.getResourcesByResTypeName(resourceType.getName(), pageNumber, pageSize);
	}
	
	@GetMapping(value = "resTypeName/{resTypeName}")
	public PageResourceDto getResourcesByResTypeName(@PathVariable("resTypeName") String resTypeName,
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		
		return resourceService.getResourcesByResTypeName(resTypeName, pageNumber, pageSize);
	}
	
	@GetMapping(value = "{id}")
	@ResponseBody
	public ResourceDto getResource(@PathVariable("id") Integer id) {
		Resource resource = resourceService.getResourceById(id);

		ResourceDto resourceDto = dtoMapper.map(resource, ResourceDto.class);
		resourceDto.setHref(uriBuilder.getHrefWithId( URL_VERSION_AND_RESOURCE_PATH, resource.getId()));
		
		return resourceDto;
	}
	
	@GetMapping(value = "name/{name}")
	@ResponseBody
	public ResourceDto getResource(@PathVariable("name") String name) {
		Resource resource = resourceService.getResourceByName(name);

		ResourceDto resourceDto = dtoMapper.map(resource, ResourceDto.class);
		resourceDto.setHref(uriBuilder.getHrefWithId( URL_VERSION_AND_RESOURCE_PATH, resource.getId()));
		
		return resourceDto;
	}
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<ResourceDto> addResource(@Valid @RequestBody ResourceDto resourceDto) {
		Resource resource = new Resource();
		URI uri;
		
		dtoMapper.map(resourceDto, resource);

		resource = resourceService.addResource(resource);
		uri = uriBuilder.requestUriWithId(resource.getId());
		
		resource.setResourceHistories(new HashSet<ResourceHistory> (resourceHistoryService.getResourceHistory(resource.getId())));
		dtoMapper.map(resource, resourceDto);
		
		resourceDto.setHref(uri.toString());
		
		return created(uri).body(resourceDto);
	}
	
	@PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResourceDto patchResource(@PathVariable("id") Integer id, @RequestBody Map<String, Object> updates) {
		return resourceService.patchResource(id, updates);
	}

	@PostMapping(value= "many")
	@ResponseBody
	public ResourcesDto addResources(@RequestBody ResourcesDto resourcesDto) {
		
		resourcesDto = resourceService.addResources(resourcesDto);
		
		return resourcesDto;
	}
	
	@PatchMapping(value = "retire/{name}")
	@ResponseBody
	public ResourceDto retireResource(@PathVariable("name") String name) {
		return resourceService.retireResource(name);
	}
	
	@PostMapping(value= "generate")
	@ResponseBody
	public ResourcesDto generateResources(@RequestBody ResourceGenerateDto resourceGenerateDto) {
		
		ResourcesDto resourcesDto = resourceService.generateResources(resourceGenerateDto);
		
		return resourcesDto;
	}	
	
	@PatchMapping(value= "reserve")
	@ResponseBody
	public List<ResourceDto> reserveResources(@RequestBody ResourceGenerateDto resourceGenerateDto) {
		
		return resourceService.reserveResources(resourceGenerateDto);
	}	
	
	@GetMapping(value = "report")
	@ResponseBody
	public List<ResourceCountDto> getResourcesReport() {

		return resourceService.getResourcesReport();
	}
	
}
