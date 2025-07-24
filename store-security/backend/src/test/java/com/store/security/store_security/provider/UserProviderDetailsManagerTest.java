package com.store.security.store_security.provider;

import com.store.security.store_security.service.UserSecurityDetailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserProviderDetailsManagerTest {

	@InjectMocks
	private UserProviderDetailsManager userProviderDetailsManager;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserSecurityDetailService userSecurityDetailService;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		userProviderDetailsManager = new UserProviderDetailsManager(userSecurityDetailService, passwordEncoder);
	}


	@Test
	public void authenticate()
	{
		//given
		Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
		Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(true);
		Mockito.when(userSecurityDetailService.loadUserByUsername("username")).thenReturn(new User("username", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"))));
		//when
		Authentication authenticationResult = userProviderDetailsManager.authenticate(authentication);
		//then
		Assertions.assertThat(authenticationResult).isNotNull();
	}

	@Test
	public void noAuthenticatePaswordEncorder() {
		//given
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"username", "password");
		Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(false);
		Mockito.when(userSecurityDetailService.loadUserByUsername("username")).thenReturn(
				new User("username", "password",
						List.of(new SimpleGrantedAuthority("ROLE_USER"))));
		//when
		//then
		Assertions.assertThatThrownBy(
						() -> userProviderDetailsManager.authenticate(authentication))
				.isInstanceOf(BadCredentialsException.class)
				.hasMessageContaining("Bad credentials");
	}

	@Test
	public void noAuthenticateUserDetails() {
		//given
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"username", "password");
		Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(false);
		Mockito.when(userSecurityDetailService.loadUserByUsername("username")).thenThrow(
				new UsernameNotFoundException("user not found"));
		//when
		//then
		Assertions.assertThatThrownBy(
						() -> userProviderDetailsManager.authenticate(authentication))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessageContaining("user not found");
	}
}
