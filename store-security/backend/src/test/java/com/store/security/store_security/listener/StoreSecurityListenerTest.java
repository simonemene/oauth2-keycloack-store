package com.store.security.store_security.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

//TODO:NOT TERMINATE
public class StoreSecurityListenerTest {

	private StoreSecurityListener listener;


	@BeforeEach
	public void init()
	{
		listener = new StoreSecurityListener();
	}


	@Test
	public void success()
	{
		//given
		Authentication auth = new UsernamePasswordAuthenticationToken("test", "test");
		AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(auth);
		//when
		listener.onAuthenticationSuccessEvent(event);
		//then
	}

	@Test
	public void failed()
	{
		//given
		Authentication auth = new UsernamePasswordAuthenticationToken("test", "test");
		AbstractAuthenticationFailureEvent event = new AbstractAuthenticationFailureEvent(auth, new BadCredentialsException("errore")){};
		//when
		listener.onAuthenticationFailureEvent(event);
		//then
	}
}
