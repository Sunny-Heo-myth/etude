package org.alan.etude.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // AuthenticationEntryPoint exception occurs before ExceptionAdvice.
    // Does not throw etude's custom exception anymore.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(SC_UNAUTHORIZED);
    }

}
