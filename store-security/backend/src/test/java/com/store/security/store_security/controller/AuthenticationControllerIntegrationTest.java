package com.store.security.store_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.controladvice.GenericExceptionHandler;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@Import(GenericExceptionHandler.class)
public class AuthenticationControllerIntegrationTest extends
		StoreSecurityApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;


	@Test
	public void registration() throws Exception {
		//given
		String json = "{"
				+ "\"username\": \"username\","
				+ "\"password\": \"1234\","
				+ "\"age\": 21,"
				+ "\"tmstInsert\": \"2025-06-18T00:00:00\""
				+ "}";


		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/registration")
						.contentType("application/json").content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(21))
				.andExpect(MockMvcResultMatchers.jsonPath("$.authoritiesList").value("ROLE_USER"));
	}

	@Test
	public void registrationFailed() throws Exception {
		//given
		String json = "{"
				+ "\"username\": \"\","
				+ "\"password\": \"1234\","
				+ "\"age\": 21,"
				+ "\"authoritiesList\": [\"ROLE_USER\"],"
				+ "\"tmstInsert\": \"2025-06-18T00:00:00\""
				+ "}";

		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/registration").contentType("application/json").content(json))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Registration failed"));
	}



}
