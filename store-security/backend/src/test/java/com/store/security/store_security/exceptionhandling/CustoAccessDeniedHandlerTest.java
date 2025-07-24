package com.store.security.store_security.exceptionhandling;

import com.store.security.store_security.exceptionhandle.CustomAccessDeniedHandler;
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
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

public class CustoAccessDeniedHandlerTest {
	@InjectMocks
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private AccessDeniedException accessDeniedException;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		customAccessDeniedHandler = new CustomAccessDeniedHandler();
	}


	@Test
	public void commence() throws IOException, ServletException {
		//given
		Mockito.when(accessDeniedException.getMessage()).thenReturn("message exception");
		Mockito.when(request.getRequestURI()).thenReturn("url");
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		//when
		customAccessDeniedHandler.handle(request, response, accessDeniedException);
		//then
		Mockito.verify(response).setStatus(HttpStatus.FORBIDDEN.value());
		Mockito.verify(response).setHeader("security-app","access denied");
		Mockito.verify(response).setContentType("application/json");
		printWriter.flush();

		Assertions.assertThat(stringWriter.toString()).contains("\"path\":\"url\"");
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"status\":\"%s\"",HttpStatus.FORBIDDEN.value()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"message\":\"%s\"",HttpStatus.FORBIDDEN.getReasonPhrase()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"error\":\"%s\"",accessDeniedException.getMessage()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"timestamp\":\"%s",
				LocalDate.now()));


	}

	@Test
	public void commenceMessageEmpty() throws IOException, ServletException {
		//given
		Mockito.when(accessDeniedException.getMessage()).thenReturn("");
		Mockito.when(request.getRequestURI()).thenReturn("url");
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		//when
		customAccessDeniedHandler.handle(request, response, accessDeniedException);
		//then
		Mockito.verify(response).setStatus(HttpStatus.FORBIDDEN.value());
		Mockito.verify(response).setHeader("security-app","access denied");
		Mockito.verify(response).setContentType("application/json");
		printWriter.flush();

		Assertions.assertThat(stringWriter.toString()).contains("\"path\":\"url\"");
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"status\":\"%s\"",HttpStatus.FORBIDDEN.value()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"message\":\"%s\"",HttpStatus.FORBIDDEN.getReasonPhrase()));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"error\":\"%s\"","Access denied"));
		Assertions.assertThat(stringWriter.toString()).contains(String.format("\"timestamp\":\"%s",
				LocalDate.now()));


	}
}
