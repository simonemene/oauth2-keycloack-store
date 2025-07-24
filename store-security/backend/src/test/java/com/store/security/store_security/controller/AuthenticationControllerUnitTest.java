package com.store.security.store_security.controller;

import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.properties.StoreProperties;
import com.store.security.store_security.service.IRegistrationService;
import com.store.security.store_security.service.IUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthenticationControllerUnitTest {


	@Mock
	private IRegistrationService registrationService;

    @Mock
	private IUserService userService;

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private StoreProperties storeProperties;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		authenticationController = new AuthenticationController(registrationService,userService,authenticationManager,storeProperties);
	}


	@Test
	public void registration()
	{
		//given
		UserDto userDto = UserDto.builder().password("1234").age(21).username("username").tmstInsert(
				LocalDateTime.now()).authoritiesList(
				List.of("ROLE_USER")).build();
		userDto.setId(1L);
		Mockito.when(registrationService.registrationUser(userDto)).thenReturn(userDto);
		//when

		ResponseEntity<UserDto> responseEntity = authenticationController.registration(userDto);
		//then
		Mockito.verify(registrationService,Mockito.times(1)).registrationUser(Mockito.any());
		Assertions.assertThat(responseEntity.getStatusCode().value()).isEqualTo(
				HttpStatus.CREATED.value());
		Assertions.assertThat(responseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(userDto);
		Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getId()).isGreaterThan(0);

	}
}
