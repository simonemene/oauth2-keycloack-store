package com.store.security.store_security.service;

import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import com.store.security.store_security.service.impl.RegistrationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RegistrationServiceUnitTest {

	@Mock
	private UserRepository userRepository;;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserMapper userMapper;

	@Mock
	private AuthoritiesRepository authoritiesRepository;

	@InjectMocks
	private RegistrationService registrationService;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		registrationService = new RegistrationService(userRepository, userMapper, passwordEncoder, authoritiesRepository);
	}
	@Test
	public void registrationAlreadyExistsFailed()
	{
		//given
		UserDto userDto = UserDto.builder().password("1234").age(18).username("username1").tmstInsert(
				LocalDateTime.now()).authoritiesList(
				List.of("ROLE_USER")).build();
		UserEntity userEntity = UserEntity.builder().id(1L).password("1234").age(18).username("username1").tmstInsert(
				LocalDateTime.now()).authoritiesList(
				Set.of(AuthoritiesEntity.builder().authority("ROLE_USER").build())).build();
		Optional<UserEntity> userEntityOptional = Optional.of(userEntity);

		Mockito.when(userRepository.findByUsername("username1")).thenReturn(userEntityOptional);
		//when
		//then
		Assertions.assertThatThrownBy(()->registrationService.registrationUser(userDto)).isInstanceOf(
				UserException.class).hasMessageContaining("User already exist");
	}


}
