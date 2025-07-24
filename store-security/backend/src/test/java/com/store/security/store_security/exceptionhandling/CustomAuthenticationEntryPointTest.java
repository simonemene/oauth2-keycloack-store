package com.store.security.store_security.exceptionhandling;

import com.store.security.store_security.exceptionhandle.CustomAuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

public class CustomAuthenticationEntryPointTest {

	@InjectMocks
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private AuthenticationException authenticationException;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
	}


	@Test
	public void commence() throws IOException, ServletException {
		//given
		Mockito.when(authenticationException.getMessage()).thenReturn("message exception");
		Mockito.when(request.getRequestURI()).thenReturn("url");
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		//when
		customAuthenticationEntryPoint.commence(request, response, authenticationException);
		//then
		Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
		Mockito.verify(response).setHeader("security-app","unauthorized");
		Mockito.verify(response).setContentType("application/json");
		printWriter.flush();

		Assertions.assertThat(stringWriter.toString()).contains("\"path\":\"url\"");
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"status\":\"%s\"",HttpStatus.UNAUTHORIZED));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"message\":\"%s\"",HttpStatus.UNAUTHORIZED.getReasonPhrase()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"error\":\"%s\"",authenticationException.getMessage()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"timestamp\":\"%s",
				LocalDate.now()));
	}

	@Test
	public void commenceMessageEmpty() throws IOException, ServletException {
		//given
		Mockito.when(authenticationException.getMessage()).thenReturn("");
		Mockito.when(request.getRequestURI()).thenReturn("url");
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		//when
		customAuthenticationEntryPoint.commence(request, response, authenticationException);
		//then
		Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
		Mockito.verify(response).setHeader("security-app","unauthorized");
		Mockito.verify(response).setContentType("application/json");
		printWriter.flush();

		Assertions.assertThat(stringWriter.toString()).contains("\"path\":\"url\"");
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"status\":\"%s\"",HttpStatus.UNAUTHORIZED));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"message\":\"%s\"",HttpStatus.UNAUTHORIZED.getReasonPhrase()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"error\":\"%s\"","Unauthorized"));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"timestamp\":\"%s",
				LocalDate.now()));
	}
}
