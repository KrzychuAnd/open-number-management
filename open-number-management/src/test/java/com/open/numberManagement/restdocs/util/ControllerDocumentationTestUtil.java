package com.open.numberManagement.restdocs.util;

import static com.open.numberManagement.jUnit.Constants.DUMMY_PERMISSION_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_RESOURCE_TYPE_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_ROLE_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_FIRST_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_LAST_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_PASSWORD;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.open.numberManagement.entity.Permission;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.entity.Role;
import com.open.numberManagement.entity.User;
import com.open.numberManagement.service.PermissionService;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.service.ResourceStatusService;
import com.open.numberManagement.service.ResourceTypeService;
import com.open.numberManagement.service.RoleService;
import com.open.numberManagement.service.UserService;

public class ControllerDocumentationTestUtil {

	private PermissionService permissionService;
	private RoleService roleService;
	private UserService userService;
	private ResourceTypeService resourceTypeService;
	private ResourceService resourceService;
	private ResourceStatusService resourceStatusService;

	private MockMvc mockMvc;

	public ControllerDocumentationTestUtil(MockMvc mockMvc, PermissionService permissionService,
			RoleService roleService, UserService userService, ResourceTypeService resourceTypeService,
			ResourceService resourceService, ResourceStatusService resourceStatusService) {
		this.mockMvc = mockMvc;
		this.permissionService = permissionService;
		this.roleService = roleService;
		this.userService = userService;
		this.resourceTypeService = resourceTypeService;
		this.resourceService = resourceService;
		this.resourceStatusService = resourceStatusService;
	}

	public Permission createDummyPermission() {
		Permission dummyPermission = new Permission(DUMMY_PERMISSION_NAME,
				"Dummy permission - access to Dummy Resource Type");
		return dummyPermission = permissionService.addPermission(dummyPermission);
	}

	public Role createDummyRole(Permission dummyPermission) {
		Role dummyRole = new Role(DUMMY_ROLE_NAME, DUMMY_ROLE_NAME);
		Set<Permission> rolePermissions = new HashSet<>();
		rolePermissions.add(dummyPermission);
		dummyRole.setPermissions(rolePermissions);
		return dummyRole = roleService.addRole(dummyRole);
	}

	public User createDummyUser(Role dummyRole, String userLogin) {
		User dummyUser = new User();
		dummyUser.setLogin(userLogin);
		dummyUser.setFirstName(DUMMY_USER_FIRST_NAME);
		dummyUser.setLastName(DUMMY_USER_LAST_NAME);
		dummyUser.setPassword(DUMMY_USER_PASSWORD);
		if (dummyRole != null) {
			dummyUser.setRoleId(dummyRole.getId());
			dummyUser.setRole(dummyRole);
		}
		dummyUser.setLocked('N');

		return dummyUser = userService.addUser(dummyUser);
	}

	public ResourceType createResourceType(Permission dummyPermission) {
		Set<Permission> resourceTypePermissions = new HashSet<>();
		resourceTypePermissions.add(dummyPermission);
		ResourceType dummyResourceType = new ResourceType(DUMMY_RESOURCE_TYPE_NAME, DUMMY_RESOURCE_TYPE_NAME, 10, 99,
				300);
		dummyResourceType.setPermissions(resourceTypePermissions);
		return dummyResourceType = resourceTypeService.addResourceType(dummyResourceType);
	}

	public Resource createDummyResource(ResourceType dummyResourceType, String dummyResourceName) {
		ResourceStatus availableResourceStatus = resourceStatusService
				.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);
		Resource dummyResource = new Resource(dummyResourceName, dummyResourceType.getId(),
				availableResourceStatus.getId());
		return dummyResource = resourceService.addResource(dummyResource);
	}

	public String obtainAccessToken(String clientId, String clientSecret, String grantType, String username,
			String password) throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("grant_type", grantType);
		params.add("username", username);
		params.add("password", password);

		ResultActions result = mockMvc
				.perform(post("/oauth/token").params(params).contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	public ResponseFieldsSnippet resourceResponseFieldsSnippet() {
		return relaxedResponseFields(fieldWithPath("id").description("Resource OpenNM internal id"),
				fieldWithPath("href").description("Direct URL to "),
				fieldWithPath("name").description(
						"Name of Resource - have to be inline with Business rules related to requested Resource Type definition, e.g. prefix, length, ..."),
				fieldWithPath("resTypeId").description("Resource Type ID"),
				fieldWithPath("resStatusId").description("Resource Status ID"),
				fieldWithPath("relResId").type("Number").optional().description("Related Resource ID"),
				fieldWithPath("descr").type("String").optional().description("Description of Resource"),
				fieldWithPath("resourceHistories").type("Array").optional().description(
						"History of Resource initialy one entry with current values of Resource Status, Related Resource ID and Description"));
	}

	public ResponseFieldsSnippet exceptionResponseFieldsSnippet() {
		return relaxedResponseFields(fieldWithPath("timestamp").description("Server timestamp"),
				fieldWithPath("status").description("HTTP Error Code"),
				fieldWithPath("error").description("HTTP Error Message"),
				fieldWithPath("exception").description("Server application exception"),
				fieldWithPath("businessCode")
						.description("Business Code of exception, please refer to Business Code list"),
				fieldWithPath("message").description("More meaningfull exception message"),
				fieldWithPath("path").description("URL path of requested HTTP resource"));
	}
}
