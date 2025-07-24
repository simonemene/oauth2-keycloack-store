package com.store.security.store_security.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.AllUserDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@AutoConfigureMockMvc
public class UserControllerIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserMapper userMapper;

	@Test
	@WithMockUser(username = "prova@gmail.com", roles = "USER")
	public void userFound() throws Exception {
		//given
		String username = "prova@gmail.com";
		AuthoritiesEntity userRole = AuthoritiesEntity.builder()
				.authority(RoleConstants.USER.getRole())
				.build();
		authoritiesRepository.save(userRole);
		UserEntity userEntity = UserEntity.builder()
				.username("prova@gmail.com")
				.age(21)
				.password("1234")
				.tmstInsert(LocalDateTime.of(2022, 1, 1, 0, 0))
				.authoritiesList(Set.of(userRole))
				.build();
		userEntity = userRepository.save(userEntity);
		//whe
		//then
		mockMvc.perform(get("/api/user/{id}",userEntity.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("prova@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(21))
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.tmstInsert").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.authoritiesList").value("ROLE_USER"));
	}

	@Test
	@WithMockUser(username = "prova@gmail.com", roles = "MACK")
	public void userNoAccess() throws Exception {
		//given
		UserEntity userEntity = UserEntity.builder().username("prova@gmail.com").age(21).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		AuthoritiesEntity userRole = AuthoritiesEntity.builder()
				.authority(RoleConstants.USER.getRole())
				.build();
		authoritiesRepository.save(userRole);

		UserEntity userEntitySave = UserEntity.builder()
				.username("prova@gmail.com")
				.age(21)
				.password("1234")
				.tmstInsert(LocalDateTime.of(2022, 1, 1, 0, 0))
				.authoritiesList(Set.of(userRole))
				.build();

		// 3. Salva l’utente
		userEntitySave = userRepository.save(userEntitySave);

		//when
		//then
		mockMvc.perform(get("/api/user/{id}",userEntitySave.getId()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Access Denied"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.FORBIDDEN.getReasonPhrase()));
	}

	@Test
	@WithMockUser(username = "prova@gmail.com", roles = "TRACKER")
	public void userNoAccessRole() throws Exception {
		//given
		String username = "prova@gmail.com";
		AuthoritiesEntity userRole = AuthoritiesEntity.builder()
				.authority(RoleConstants.USER.getRole())
				.build();
		authoritiesRepository.save(userRole);

		UserEntity userEntityResult = UserEntity.builder()
				.username("prova@gmail.com")
				.age(21)
				.password("1234")
				.tmstInsert(LocalDateTime.of(2022, 1, 1, 0, 0))
				.authoritiesList(Set.of(userRole))
				.build();

		// 3. Salva l’utente
		userEntityResult = userRepository.save(userEntityResult);

		UserDto userDto = UserDto.builder().username(username).age(21).build();
		String json = objectMapper.writeValueAsString(userDto);
		//when
		//then
		mockMvc.perform(get("/api/user/{id}",userEntityResult.getId()))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Access Denied"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.FORBIDDEN.getReasonPhrase()));
	}

	@Test
	@WithMockUser(username = "prova@gmail.com", roles = "ADMIN")
	public void allUser() throws Exception {
		//given
		AuthoritiesEntity authoritiesEntity = authoritiesRepository
				.findByAuthority(RoleConstants.USER.getRole())
				.orElseThrow(() -> new UserException("Authorization USER not found"));
		UserEntity userEntity = UserEntity.builder().username("prova@gmail.com").age(21).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		userEntity.setAuthoritiesList(Set.of(authoritiesEntity));
		userRepository.save(userEntity);
		//whe
		//then
		MvcResult result = mockMvc.perform(get("/api/user"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();

		ObjectMapper objectMapper = new ObjectMapper();
		AllUserDto users = objectMapper.readValue(json, new TypeReference<AllUserDto>() {});
		UserDto provagmail = users.getUsers().stream().filter(user->user.getUsername().equals("prova@gmail.com")).findAny()
				.get();
		Assertions.assertThat(provagmail).usingRecursiveComparison()
				.ignoringFields("password", "tmstInsert", "authoritiesList")
				.isEqualTo(userMapper.toDto(userEntity));
		Assertions.assertThat(provagmail.getPassword()).isNull();
	}

}
