package com.open.numberManagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.service.ResourceStatusService;
import com.open.numberManagement.service.ResourceTypeService;

import org.apache.commons.lang3.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(true)
public class ResourceServiceTests {

	@Autowired
	private ResourceTypeService resourceTypeService;
	
	@Autowired
	private ResourceStatusService resourceStatusService;
	
	@Autowired
	private ResourceService resourceService;
	
	private ResourceType dummyResourceType;
	private ResourceStatus dummyResourceStatus;
	private Resource dummyResource;
	
	@Before
	public void setUp() {
		//Add dummy Resource type
		dummyResourceType = new ResourceType("DUMMY_RES_TYPE", "Dummy resource type", 10, 99, 300);
		dummyResourceType = this.resourceTypeService.addResourceType(dummyResourceType);
		
		//Add dummy Resource status
		dummyResourceStatus = new ResourceStatus("DUMMY_RES_STATUS", "Dummy resource status");
		dummyResourceStatus = this.resourceStatusService.addResourceType(dummyResourceStatus);
		
		//Add dummy Resource
		dummyResource = new Resource("1230560890", dummyResourceType.getId(), dummyResourceStatus.getId());
		dummyResource = this.resourceService.addResource(dummyResource);
	}
		
	@Test
	@Transactional
	public void addResource() throws Exception{
		String name = dummyResourceType.getPrefix() + StringUtils.leftPad("999999", (dummyResourceType.getLength() - 2), "0");
		Resource resource = new Resource(name, dummyResourceType.getId(), dummyResourceStatus.getId());
		this.resourceService.addResource(resource);
		
		assertNotEquals(null, resource.getId());
	}
	
	@Test
	@Transactional
	public void getResourceById() throws Exception{
		Resource resource = this.resourceService.getResourceById(dummyResource.getId());
		
		assertNotEquals(null, resource.getId());
	}
	
	@Test
	@Transactional
	public void getResourceByName() throws Exception{
		Resource resource = this.resourceService.getResourceByName(dummyResource.getName());
		
		assertNotEquals(null, resource.getId());
	}	
	
	@Test
	@Transactional
	public void getResourcesByResTypeId() throws Exception{
		List<Resource> resources = this.resourceService.getResourcesByResTypeId(dummyResourceType.getId());
		
		assertNotEquals(null, resources.get(0).getId());
	}		
	
	@Test
	@Transactional
	public void getResourceByResTypeName() throws Exception{
		List<Resource> resources = this.resourceService.getResourcesByResTypeName(dummyResourceType.getName());
		
		assertNotEquals(null, resources.get(0).getId());
	}			
	
	@Test
	@Transactional
	public void deleteResource() {
		Resource testResource;
		this.resourceService.deleteResource(dummyResource);
		testResource = this.resourceService.getResourceById(dummyResource.getId());
		
		assertEquals(null, testResource);
	}
}
