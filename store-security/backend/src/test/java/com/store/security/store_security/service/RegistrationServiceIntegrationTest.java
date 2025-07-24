package com.store.security.store_security.service;

import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.service.impl.RegistrationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

public class RegistrationServiceIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;


	@Test
	public void registration()
	{
		//given
		authoritiesRepository.save(AuthoritiesEntity.builder().authority("ROLE_USER").build());
		UserDto userDto = UserDto.builder().authoritiesList(List.of(RoleConstants.USER.getRole())).username("username").age(29).tmstInsert(
				LocalDateTime.of(2022, 1, 1, 1, 1)).password("1234").build();
		//when
		UserDto result = registrationService.registrationUser(userDto);
		//then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isGreaterThan(0);
		Assertions.assertThat(result).usingRecursiveComparison().ignoringFields("id","password").isEqualTo(userDto);
	}


}
