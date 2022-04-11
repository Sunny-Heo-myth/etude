package com.chatboard.etude.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // AuthenticationEntryPoint exception occurs before ExceptionAdvice.
    // Does not return Response Object of etude.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(SC_UNAUTHORIZED);
    }

}
