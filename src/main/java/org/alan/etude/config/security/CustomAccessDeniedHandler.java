package org.alan.etude.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // AccessDeniedHandler exception occurs before ExceptionAdvice.
    // Does not throw etude's custom exception.
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        response.setStatus(SC_FORBIDDEN);
    }

}
