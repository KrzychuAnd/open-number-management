package com.open.numberManagement.service;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.open.numberManagement.dto.entity.PageResourceDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.exception.ResourceNotFoundException;
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
	private ResourceStatus newResourceStatus;
	private Resource dummyResource;

	@Before
	public void setUp() {
		// Add dummy Resource type
		dummyResourceType = new ResourceType("DUMMY_RES_TYPE", "Dummy resource type", 10, 99, 300);
		dummyResourceType = this.resourceTypeService.addResourceType(dummyResourceType);

		// Get Available Resource status
		newResourceStatus = this.resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);

		// Add dummy Resource
		dummyResource = new Resource("9912345678", dummyResourceType.getId(), newResourceStatus.getId());
		dummyResource = this.resourceService.addResource(dummyResource);
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void addResource() throws Exception {
		String name = dummyResourceType.getPrefix()
				+ StringUtils.leftPad("999999", (dummyResourceType.getLength() - 2), "0");
		Resource resource = new Resource(name, dummyResourceType.getId(), newResourceStatus.getId());
		this.resourceService.addResource(resource);

		assertNotEquals(null, resource.getId());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceById() throws Exception {
		Resource resource = this.resourceService.getResourceById(dummyResource.getId());

		assertNotEquals(null, resource.getId());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByName() throws Exception {
		Resource resource = this.resourceService.getResourceByName(dummyResource.getName());

		assertNotEquals(null, resource.getId());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourcesByResTypeId() throws Exception {
		List<Resource> resources = this.resourceService.getResourcesByResTypeId(dummyResourceType.getId());

		assertNotEquals(null, resources.get(0).getId());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByResTypeName() throws Exception {
		PageResourceDto pageResources = this.resourceService.getResourcesByResTypeName(dummyResourceType.getName(), 0, 10);

		assertNotEquals(null, pageResources.getResources().get(0).getId());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void deleteResource() {
		Resource testResource;
		this.resourceService.deleteResource(dummyResource);
		try {
			testResource = this.resourceService.getResourceById(dummyResource.getId());
		} catch (ResourceNotFoundException e) {
			testResource = null;
		}

		assertEquals(null, testResource);
	}
}
