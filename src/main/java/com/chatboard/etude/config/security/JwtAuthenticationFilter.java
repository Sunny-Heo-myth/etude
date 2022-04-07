package com.chatboard.etude.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    // Only when access token is valid, save user info in context.
    // verifying refresh token & read user info function deleted.

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        extractToken(request)
                .map(userDetailsService::loadUserByUsername)
                        .ifPresent(this::setAuthentication);
        chain.doFilter(request, response);
    }

    private void setAuthentication(CustomUserDetails userDetails) {
        // set CustomUserDetail in Security context
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request)   // casting first bracket.
                .getHeader("Authorization"));
    }

    // if non-null, otherwise returns an empty Optional(new Optional<>()).
}
