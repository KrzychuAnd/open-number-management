package com.open.numberManagement.restdocs.util;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ControllerDocumentationTestUtil {

	private MockMvc mockMvc;

	public ControllerDocumentationTestUtil(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
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
}
