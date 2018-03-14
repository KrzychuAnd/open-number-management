package com.open.numberManagement.resources.get;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.ADMINISTRATOR_USER;
import static com.open.numberManagement.util.Constants.DIRECTORY_GENERATED_SNIPPETS;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;

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
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	ControllerDocumentationTestUtil cdtUtil;

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

		cdtUtil = new ControllerDocumentationTestUtil(this.mockMvc, permissionService,
				roleService, userService, resourceTypeService, resourceService, resourceStatusService);

		// Add dummy Permission
		dummyPermission = cdtUtil.createDummyPermission();

		// Add Role
		dummyRole = cdtUtil.createDummyRole(dummyPermission);

		// Add dummy User
		dummyUser = cdtUtil.createDummyUser(dummyRole, DUMMY_USER_LOGIN);
		
		// Add dummy User No permission
		dummyUserNoPerm = cdtUtil.createDummyUser(null, DUMMY_USER_LOGIN_NO_PERM);

		// Add dummy Resource type
		dummyResourceType = cdtUtil.createResourceType(dummyPermission);

		// Get Available Resource status
		availableResourceStatus = this.resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);

		// Add dummy Resource
		dummyResource = cdtUtil.createDummyResource(dummyResourceType, "9912345678");

		// Obtain access token
		try {
			dummyUserAccessToken = cdtUtil.obtainAccessToken(clientId, clientSecret, grantType, DUMMY_USER_LOGIN,
					DUMMY_USER_PASSWORD);
			dummyUserNoPermAccessToken = cdtUtil.obtainAccessToken(clientId, clientSecret, grantType,
					DUMMY_USER_LOGIN_NO_PERM, DUMMY_USER_PASSWORD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByIdOk() throws Exception {

		this.mockMvc
				.perform(get("/" + URL_VERSION_AND_RESOURCE_PATH + "{id}", dummyResource.getId())
						.header("Authorization", "Bearer " + dummyUserAccessToken)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("{method-name}",
						pathParameters(parameterWithName("id").description("Resource id to retrieve")),
						(cdtUtil.resourceResponseFieldsSnippet())));
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByIdBadRequest() throws Exception {

		this.mockMvc
				.perform(get("/" + URL_VERSION_AND_RESOURCE_PATH + "{id}", "invalid_res_id")
						.header("Authorization", "Bearer " + dummyUserAccessToken)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andDo(document("{method-name}",
						pathParameters(parameterWithName("id").description("Resource id to retrieve")),
						(cdtUtil.exceptionResponseFieldsSnippet())));
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByIdUnauthorized() throws Exception {

		this.mockMvc
				.perform(get("/" + URL_VERSION_AND_RESOURCE_PATH + "{id}", dummyResource.getId())
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andDo(document("{method-name}",
						pathParameters(parameterWithName("id").description("Resource id to retrieve")),
						(relaxedResponseFields(fieldWithPath("error").description("Error"),
								fieldWithPath("error_description").description("Error Description")))));
	}

	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByIdForbidden() throws Exception {

		this.mockMvc
				.perform(get("/" + URL_VERSION_AND_RESOURCE_PATH + "{id}", dummyResource.getId())
						.header("Authorization", "Bearer " + dummyUserNoPermAccessToken)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andDo(document("{method-name}",
						pathParameters(parameterWithName("id").description("Resource id to retrieve")),
						(cdtUtil.exceptionResponseFieldsSnippet())));
	}
	
	@Test
	@WithMockUser(username = ADMINISTRATOR_USER, authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void getResourceByIdNotFound() throws Exception {

		this.mockMvc
				.perform(get("/" + URL_VERSION_AND_RESOURCE_PATH + "{id}", 123456789 /* Resource does not exists */)
						.header("Authorization", "Bearer " + dummyUserAccessToken)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(document("{method-name}",
						pathParameters(parameterWithName("id").description("Resource id to retrieve")),
						(cdtUtil.exceptionResponseFieldsSnippet())));
	}	
}