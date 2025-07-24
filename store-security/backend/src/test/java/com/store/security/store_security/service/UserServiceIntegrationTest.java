package com.store.security.store_security.service;

import com.store.security.store_security.StoreSecurityApplicationTests;
import com.store.security.store_security.dto.AllUserDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UserServiceIntegrationTest extends StoreSecurityApplicationTests {

	@Autowired
	private IUserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	private UserMapper userMapper;

	@Transactional
	@Test
	public void allUser()
	{
		//given
		UserEntity userEntity = UserEntity.builder().username("username").age(21).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		AuthoritiesEntity authoritiesEntity = AuthoritiesEntity.builder().authority("ROLE_USER").users(Set.of(userEntity)).build();
		userEntity.setAuthoritiesList(Set.of(authoritiesEntity));
		UserEntity userEntity1 = UserEntity.builder().username("username1").age(22).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		userEntity1.setAuthoritiesList(Set.of(authoritiesEntity));
		userRepository.save(userEntity1);
		userRepository.save(userEntity);
		authoritiesRepository.save(authoritiesEntity);
		//when
		AllUserDto user = userService.allUser();
		//then
		//admin is always present for class DataConfigInit
		Assertions.assertThat(user.getUsers().size()).isEqualTo(2);
		UserDto username = user.getUsers().stream().filter(u->u.getUsername().equals("username")).findAny()
				.get();
		Assertions.assertThat(username).usingRecursiveComparison().isEqualTo(userMapper.toDto(userEntity));
	}


	@Test
	public void findUser()
	{
		//given
		UserEntity userEntity = UserEntity.builder().username("username").age(21).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		AuthoritiesEntity authoritiesEntity = AuthoritiesEntity.builder().authority("ROLE_USER").users(Set.of(userEntity)).build();
		userEntity.setAuthoritiesList(Set.of(authoritiesEntity));
		authoritiesRepository.save(authoritiesEntity);
		userRepository.save(userEntity);

		//when
		UserDto user = userService.findUser(userEntity.getId());
		//then
		Assertions.assertThat(user.getPassword()).isEqualTo(userEntity.getPassword());
		Assertions.assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
		for(String auth: user.getAuthoritiesList())
		{
			AuthoritiesEntity firstAuthority = userEntity.getAuthoritiesList().iterator().next();
			Assertions.assertThat(auth).isEqualTo(firstAuthority.getAuthority());

		}
		Assertions.assertThat(user.getAge()).isEqualTo(userEntity.getAge());
		Assertions.assertThat(user.getTmstInsert()).isEqualTo(userEntity.getTmstInsert());
	}

	@Test
	public void userNotFound()
	{
		//given


		UserEntity userEntity = UserEntity.builder().id(1L).username("username").age(21).password("1234").tmstInsert(
				LocalDateTime.of(2022, 1, 1, 0, 0)).build();
		AuthoritiesEntity authoritiesEntity = AuthoritiesEntity.builder().authority("ROLE_USER").users(Set.of(userEntity)).build();
		userEntity.setAuthoritiesList(Set.of(authoritiesEntity));
		//when
		//then
		Assertions.assertThatThrownBy(()->userService.findUser(userEntity.getId()))
				.isInstanceOf(UserException.class)
				.hasMessageContaining("User 1 not found");
	}
}
