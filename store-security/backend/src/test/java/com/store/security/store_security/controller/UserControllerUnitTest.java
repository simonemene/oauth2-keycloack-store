package com.store.security.store_security.controller;

import com.store.security.store_security.controladvice.GenericExceptionHandler;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import(GenericExceptionHandler.class)
public class UserControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;


	@Test
	@WithMockUser(username = "admin@gmail.com", roles = "USER")
	public void userNotFound() throws Exception {
		//given
		Mockito.when(userService.findUser(1L)).thenThrow(new UserException("User admin not found"));
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}",1L))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("User admin not found"));
	}

	@Test
	@WithMockUser(username = "admin@gmail.com", roles = "USER")
	public void allUserNotFound() throws Exception {
		//given
		String username = "admin@gmail.com";
		Mockito.when(userService.allUser()).thenThrow(new UserException("All user not found"));
		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("All user not found"));
	}


}
