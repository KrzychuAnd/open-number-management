package com.open.numberManagement;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.numberManagement.entity.Permission;
import com.open.numberManagement.service.PermissionService;
import com.open.numberManagement.restdocs.util.ConstrainedFields;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(true)
public class PermissionsControllerDocumentationTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	private RestDocumentationResultHandler documentationHandler;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PermissionService permissionService;

	@Before
	public void setUp() {
		this.documentationHandler = document("{method-name}",
				preprocessRequest(/* removeHeaders("Authorization"), */ prettyPrint()),
				preprocessResponse(prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).alwaysDo(documentationHandler).build();
	}

	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	@Transactional
	public void addPermission() throws Exception {

		Permission permission = new Permission("TEMP_PERM", "Temporary permission");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/v1/permissions")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(permission));

		ConstrainedFields fields = new ConstrainedFields(Permission.class);
		
		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(document("add-permission",
						(relaxedRequestFields(fields.withPath("name").description("Name of Permission"),
								fields.withPath("descr", "Optional").description("Description of Permission"))),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Permission"),
								fieldWithPath("descr").description("Description of Permission"),
								fieldWithPath("_links").description("Links to created resource")))));

	}

	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	public void getPermissionByName() throws Exception {
		this.mockMvc.perform(get("/v1/permissions/search/byname").param("name", ADMINISTRATOR_PERMISSION)).andExpect(status().isOk())
				.andDo(document("get-permission-by-name",
						requestParameters(parameterWithName("name").description("Permission name to retrieve")),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Permission"),
								fieldWithPath("descr").description("Description of Permission"),
								fieldWithPath("_links").description("Links to retrieve this Permission by id")))));
	}

	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	public void getPermissionById() throws Exception {
		this.mockMvc.perform(get("/v1/permissions/{id}", this.permissionService.getPermissionByName(ADMINISTRATOR_PERMISSION).getId())).andExpect(status().isOk())
				.andDo(document("get-permission-by-id",
						pathParameters(parameterWithName("id").description("Permission id to retrieve")),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Permission"),
								fieldWithPath("descr").description("Description of Permission"),
								fieldWithPath("_links").description("Links to retrieve this Permission by id")))));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	@Transactional
	public void deletePermissionById() throws Exception {
		this.mockMvc.perform(delete("/v1/permissions/{id}", this.permissionService.getPermissionByName(ADMINISTRATOR_PERMISSION).getId())).andExpect(status().isNoContent())
				.andDo(document("delete-permission-by-id",
						pathParameters(parameterWithName("id").description("Permission id to delete"))));
	}
}