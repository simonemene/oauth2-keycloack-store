package com.store.security.store_security.service;

import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserSecurityDeatilsServiceUnitTest {

	@InjectMocks
	private UserSecurityDetailService service;

	@Mock
	private UserRepository userRepository;


	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		service = new UserSecurityDetailService(userRepository);
	}


	@Test
	public void foundUser()
	{
		//given
		String username = "username";
		AuthoritiesEntity authoritiesEntity = new AuthoritiesEntity();
		authoritiesEntity.setAuthority("ROLE_USER");
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setUsername("username");
		user.setPassword("fsdfgdgfgdf");
		user.setAuthoritiesList(Set.of(authoritiesEntity));
		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		//when
		UserDetails result = service.loadUserByUsername(username);
		//then
		Assertions.assertThat(result).isInstanceOf(User.class);
		Assertions.assertThat(result.getPassword()).isEqualTo(user.getPassword());
		Assertions.assertThat(result.getAuthorities()).size().isEqualTo(1);
		Assertions.assertThat(result.getAuthorities()).isEqualTo(
				Set.of(new SimpleGrantedAuthority("ROLE_USER")));
		Assertions.assertThat(result.getUsername()).isEqualTo(user.getUsername());
	}

	@Test
	public void notFoundUser()
	{
		//given
		String username = "username";
		AuthoritiesEntity authoritiesEntity = new AuthoritiesEntity();
		authoritiesEntity.setAuthority("ROLE_USER");
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setUsername("username");
		user.setPassword("fsdfgdgfgdf");
		user.setAuthoritiesList(Set.of(authoritiesEntity));
		Mockito.when(userRepository.findByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));
		//when
		//then
		Assertions.assertThatThrownBy(()->service.loadUserByUsername(username))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessageContaining("User not found");
	}
}
