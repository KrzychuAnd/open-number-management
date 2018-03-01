package com.open.numberManagement;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.open.numberManagement.entity.Role;
import com.open.numberManagement.service.RoleService;;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(true)
public class RolesControllerDocumentationTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	private RestDocumentationResultHandler documentationHandler;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RoleService roleService;

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
	public void addRole() throws Exception {

		Role role = new Role("TEMP_ROLE", "Temporary role");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/v1/roles")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role));

		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(document("add-role", 
						(relaxedRequestFields(fieldWithPath("name").description("Name of Role"),
								fieldWithPath("descr").description("Description of Role"))),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Role"),
								fieldWithPath("descr").description("Description of Role"),
								fieldWithPath("_links").description("Links to created resource")))));

	}

	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	public void getRoleByName() throws Exception {
		this.mockMvc.perform(get("/v1/roles/search/byname").param("name", "ADMIN")).andExpect(status().isOk())
				.andDo(document("get-role-by-name",
						requestParameters(parameterWithName("name").description("Role name to retrieve")),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Role"),
								fieldWithPath("descr").description("Description of Role"),
								fieldWithPath("_links").description("Links to retrieve this Role by id")))));
	}

	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	public void getRoleById() throws Exception {
		this.mockMvc.perform(get("/v1/roles/{id}", this.roleService.getRoleByName("ADMIN").getId())).andExpect(status().isOk())
				.andDo(document("get-role-by-id",
						pathParameters(parameterWithName("id").description("Role id to retrieve")),
						(relaxedResponseFields(fieldWithPath("name").description("Name of Role"),
								fieldWithPath("descr").description("Description of Role"),
								fieldWithPath("_links").description("Links to retrieve this Role by id")))));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities= {ADMINISTRATOR_PERMISSION})
	@Transactional
	public void deleteRoleById() throws Exception {
		this.mockMvc.perform(delete("/v1/roles/{id}", this.roleService.getRoleByName("ADMIN").getId())).andExpect(status().isNoContent())
				.andDo(document("delete-role-by-id",
						pathParameters(parameterWithName("id").description("Role id to delete"))));
	}	
}
