package com.chatboard.etude.config.security;

import com.chatboard.etude.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // AccessDeniedHandler exception occurs before ExceptionAdvice.
    // Does not return Response Object of etude.
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        response.setStatus(SC_FORBIDDEN);
    }

}
