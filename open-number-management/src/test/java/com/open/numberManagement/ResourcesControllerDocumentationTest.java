package com.open.numberManagement;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.ADMINISTRATOR_USER;
import static com.open.numberManagement.util.Constants.DIRECTORY_GENERATED_SNIPPETS;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;

import static com.open.numberManagement.jUnit.Constants.DUMMY_DESCRIPTION;
import static com.open.numberManagement.jUnit.Constants.DUMMY_PERMISSION_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_RESOURCE_TYPE_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_ROLE_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_FIRST_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_LAST_NAME;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_LOGIN;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_LOGIN_NO_PERM;
import static com.open.numberManagement.jUnit.Constants.DUMMY_USER_PASSWORD;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.snippet.Attributes.key;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.numberManagement.dto.entity.ResourceDto;
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
import com.open.numberManagement.restdocs.util.ConstrainedFields;
import com.open.numberManagement.restdocs.util.ControllerDocumentationTestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(true)
public class ResourcesControllerDocumentationTest {

	@Value("${security.jwt.client-id}")
	private String clientId;

	@Value("${security.jwt.client-secret}")
	private String clientSecret;

	@Value("${security.jwt.grant-type}")
	private String grantType;

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DIRECTORY_GENERATED_SNIPPETS);

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private RestDocumentationResultHandler documentationHandler;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceTypeService resourceTypeService;

	@Autowired
	private ResourceStatusService resourceStatusService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private UserService userService;

	private Role dummyRole;
	private User dummyUser, dummyUserNoPerm;
	private ResourceType dummyResourceType;
	private ResourceStatus availableResourceStatus;
	private Resource dummyResource;
	private Permission dummyPermission;

	private String dummyUserAccessToken;
	private String dummyUserNoPermAccessToken;

	@Before
	public void setUp() {
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		this.documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).addFilter(springSecurityFilterChain)
				.apply(documentationConfiguration(this.restDocumentation)).alwaysDo(documentationHandler).build();

		ControllerDocumentationTestUtil cdtutil = new ControllerDocumentationTestUtil(this.mockMvc);

		// Add dummy Permission
		dummyPermission = new Permission(DUMMY_PERMISSION_NAME, "Dummy permission - access to Dummy Resource Type");
		dummyPermission = this.permissionService.addPermission(dummyPermission);

		// Add Role
		dummyRole = new Role(DUMMY_ROLE_NAME, DUMMY_ROLE_NAME);
		Set<Permission> rolePermissions = new HashSet<>();
		rolePermissions.add(dummyPermission);
		dummyRole.setPermissions(rolePermissions);
		dummyRole = roleService.addRole(dummyRole);

		// Add dummy User
		dummyUser = new User();
		dummyUser.setLogin(DUMMY_USER_LOGIN);
		dummyUser.setFirstName(DUMMY_USER_FIRST_NAME);
		dummyUser.setLastName(DUMMY_USER_LAST_NAME);
		dummyUser.setPassword(DUMMY_USER_PASSWORD);
		dummyUser.setRoleId(dummyRole.getId());
		dummyUser.setRole(dummyRole);
		dummyUser.setLocked('N');
		dummyUser = userService.addUser(dummyUser);

		// Add dummy User No permission
		dummyUserNoPerm = new User();
		dummyUserNoPerm.setLogin(DUMMY_USER_LOGIN_NO_PERM);
		dummyUserNoPerm.setFirstName(DUMMY_USER_FIRST_NAME);
		dummyUserNoPerm.setLastName(DUMMY_USER_LAST_NAME);
		dummyUserNoPerm.setPassword(DUMMY_USER_PASSWORD);
		dummyUserNoPerm.setRoleId(null);
		dummyUserNoPerm.setLocked('N');
		dummyUserNoPerm = userService.addUser(dummyUserNoPerm);

		// Add dummy Resource type
		Set<Permission> resourceTypePermissions = new HashSet<>();
		resourceTypePermissions.add(dummyPermission);
		dummyResourceType = new ResourceType(DUMMY_RESOURCE_TYPE_NAME, DUMMY_RESOURCE_TYPE_NAME, 10, 99, 300);
		dummyResourceType.setPermissions(resourceTypePermissions);
		dummyResourceType = this.resourceTypeService.addResourceType(dummyResourceType);

		// Get Available Resource status
		availableResourceStatus = this.resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);

		// Add dummy Resource
		dummyResource = new Resource("9912345678", dummyResourceType.getId(), availableResourceStatus.getId());
		dummyResource = this.resourceService.addResource(dummyResource);

		// Obtain access token
		try {
			dummyUserAccessToken = cdtutil.obtainAccessToken(clientId, clientSecret, grantType, DUMMY_USER_LOGIN,
					DUMMY_USER_PASSWORD);
			dummyUserNoPermAccessToken = cdtutil.obtainAccessToken(clientId, clientSecret, grantType,
					DUMMY_USER_LOGIN_NO_PERM, DUMMY_USER_PASSWORD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void addResourceCreated() throws Exception {

		String name = dummyResourceType.getPrefix()
				+ StringUtils.leftPad("999999", (dummyResourceType.getLength() - 2), "0");

		ResourceDto resource = new ResourceDto(name, dummyResourceType.getId(), availableResourceStatus.getId(),
				DUMMY_DESCRIPTION, dummyResource.getId());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/" + URL_VERSION_AND_RESOURCE_PATH)
				.header("Authorization", "Bearer " + dummyUserAccessToken).contentType(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resource));

		ConstrainedFields fields = new ConstrainedFields(ResourceDto.class);

		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(document("{method-name}", (relaxedRequestFields(fields.withPath("name").description(
						"Name of Resource - have to be inline with Business rules related to requested Resource Type definition, e.g. prefix, length, ..."),
						fields.withPath("resTypeId").description("Resource Type ID"),
						fields.withPath("resStatusId").description("Resource Status ID"),
						fields.withPath("relResId", "Optional").optional().description("Related Resource ID"),
						fields.withPath("descr", "Optional").optional().description("Description of Resource"))),
						(relaxedResponseFields(fieldWithPath("id").description("Resource OpenNM internal id"),
								fieldWithPath("href").description("Direct URL to "),
								fieldWithPath("name").description(
										"Name of Resource - have to be inline with Business rules related to requested Resource Type definition, e.g. prefix, length, ..."),
								fieldWithPath("resTypeId").description("Resource Type ID"),
								fieldWithPath("resStatusId").description("Resource Status ID"),
								fieldWithPath("relResId").description("Related Resource ID"),
								fieldWithPath("descr").description("Description of Resource"),
								fieldWithPath("resourceHistories").description(
										"History of Resource initialy one entry with current values of Resource Status, Related Resource ID and Description")))));
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void addResourceBadRequest() throws Exception {
		ResourceDto resource = new ResourceDto("", dummyResourceType.getId(), availableResourceStatus.getId(),
				DUMMY_DESCRIPTION, dummyResource.getId());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/" + URL_VERSION_AND_RESOURCE_PATH)
				.header("Authorization", "Bearer " + dummyUserAccessToken).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content("").content(objectMapper.writeValueAsString(resource));

		ConstrainedFields fields = new ConstrainedFields(ResourceDto.class);

		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andDo(document("{method-name}", (relaxedRequestFields(
						fields.withPath("resTypeId").description("Resource Type ID"),
						fields.withPath("resStatusId").description("Resource Status ID"),
						fields.withPath("relResId", "Optional").optional().description("Related Resource ID"),
						fields.withPath("descr", "Optional").optional().description("Description of Resource"))),
						(exceptionResponseFieldsSnippet())));
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void addResourceForbidden() throws Exception {

		String name = dummyResourceType.getPrefix()
				+ StringUtils.leftPad("999999", (dummyResourceType.getLength() - 2), "0");

		ResourceDto resource = new ResourceDto(name, dummyResourceType.getId(), availableResourceStatus.getId(),
				DUMMY_DESCRIPTION, dummyResource.getId());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/" + URL_VERSION_AND_RESOURCE_PATH)
				.header("Authorization", "Bearer " + dummyUserNoPermAccessToken).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(resource));

		ConstrainedFields fields = new ConstrainedFields(ResourceDto.class);

		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isForbidden())
				.andDo(document("{method-name}", (relaxedRequestFields(fields.withPath("name").description(
						"Name of Resource - have to be inline with Business rules related to requested Resource Type definition, e.g. prefix, length, ..."),
						fields.withPath("resTypeId").description("Resource Type ID"),
						fields.withPath("resStatusId").description("Resource Status ID"),
						fields.withPath("relResId", "Optional").optional().description("Related Resource ID"),
						fields.withPath("descr", "Optional").optional().description("Description of Resource"))),
						(exceptionResponseFieldsSnippet())));
	}

	private ResponseFieldsSnippet exceptionResponseFieldsSnippet() {
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