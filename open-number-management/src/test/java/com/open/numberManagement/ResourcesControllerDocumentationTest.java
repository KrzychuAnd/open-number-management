package com.open.numberManagement;

import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;
import static com.open.numberManagement.util.Constants.DIRECTORY_GENERATED_SNIPPETS;
import static com.open.numberManagement.util.Constants.DIRECTORY_SNIPPET_ADD_RESOURCE_CREATED;
import static com.open.numberManagement.util.Constants.RESOURCE_STATUS_AVAILABLE;
import static com.open.numberManagement.util.Constants.URL_VERSION_AND_RESOURCE_PATH;
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
import static org.springframework.restdocs.snippet.Attributes.key;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.numberManagement.dto.entity.ResourceDto;
import com.open.numberManagement.entity.Resource;
import com.open.numberManagement.entity.ResourceStatus;
import com.open.numberManagement.entity.ResourceType;
import com.open.numberManagement.service.ResourceService;
import com.open.numberManagement.service.ResourceStatusService;
import com.open.numberManagement.service.ResourceTypeService;
import com.open.numberManagement.restdocs.util.ConstrainedFields;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(true)
public class ResourcesControllerDocumentationTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DIRECTORY_GENERATED_SNIPPETS);

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	private RestDocumentationResultHandler documentationHandler;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceTypeService resourceTypeService;

	@Autowired
	private ResourceStatusService resourceStatusService;

	private ResourceType dummyResourceType;
	private ResourceStatus availableResourceStatus;
	private Resource dummyResource;

	@Before
	public void setUp() {
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		this.documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).alwaysDo(documentationHandler).build();

		// Add dummy Resource type
		dummyResourceType = new ResourceType("DUMMY_RES_TYPE", "Dummy resource type", 10, 99, 300);
		dummyResourceType = this.resourceTypeService.addResourceType(dummyResourceType);

		// Get Available Resource status
		availableResourceStatus = this.resourceStatusService.getResourceStatusByName(RESOURCE_STATUS_AVAILABLE);

		// Add dummy Resource
		dummyResource = new Resource("9912345678", dummyResourceType.getId(), availableResourceStatus.getId());
		dummyResource = this.resourceService.addResource(dummyResource);
	}

	@Test
	@WithMockUser(username = "admin", authorities = { ADMINISTRATOR_PERMISSION })
	@Transactional
	public void addResource() throws Exception {

		String name = dummyResourceType.getPrefix()
				+ StringUtils.leftPad("999999", (dummyResourceType.getLength() - 2), "0");

		ResourceDto resource = new ResourceDto(name, dummyResourceType.getId(), availableResourceStatus.getId(),
				"Some description", dummyResource.getId());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/" + URL_VERSION_AND_RESOURCE_PATH)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resource));

		ConstrainedFields fields = new ConstrainedFields(ResourceDto.class);

		this.mockMvc.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(document(DIRECTORY_SNIPPET_ADD_RESOURCE_CREATED, (relaxedRequestFields(
						fields.withPath("name").description(
								"Name of Resource - have to be inline with Business rules related to requested Resource Type definition, e.g. prefix, length, ..."),
						fields.withPath("resTypeId").description("Resource Type ID"),
						fields.withPath("resStatusId").description("Resource Status ID"),
						fields.withPath("relResId", "Optional").optional().description("Related Resource ID"),
						fields.withPath("descr", "Optional").optional().description("Description of Resource")))));
	}

}