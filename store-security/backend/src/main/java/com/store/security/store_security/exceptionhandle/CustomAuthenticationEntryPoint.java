package com.store.security.store_security.exceptionhandle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (authException != null && !authException.getMessage().isEmpty()) ? authException.getMessage() : "Unauthorized";
        String status = HttpStatus.UNAUTHORIZED.toString();
        String message = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        String path = request.getRequestURI();
        String jsonResponse = String.format("{\"timestamp\":\"%s\",\"error\":\"%s\",\"status\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                LocalDateTime.now(),exception,status,message,path);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("security-app","unauthorized");
        response.setContentType("application/json");

        response.getWriter().write(jsonResponse);
    }
}
