package com.store.security.store_security.exceptionhandle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String status = String.valueOf(HttpStatus.FORBIDDEN.value());
        LocalDateTime localDateTime = LocalDateTime.now();
        String path = request.getRequestURI();
        String exception = (null != accessDeniedException && !accessDeniedException.getMessage().isEmpty()) ? accessDeniedException.getMessage() : "Access denied";
        String message = HttpStatus.FORBIDDEN.getReasonPhrase();
        String jsonResponse = String.format("{\"timestamp\":\"%s\",\"error\":\"%s\",\"status\":\"%s\"," +
                "\"message\":\"%s\",\"path\":\"%s\"}",localDateTime,exception,status,message,path);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader("security-app","access denied");
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);


    }
}
