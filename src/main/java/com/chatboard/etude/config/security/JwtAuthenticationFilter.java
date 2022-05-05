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

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        extractToken(request)
                .map(userDetailsService::loadUserByUsername)    // return CustomUserDetail for authentication
                        .ifPresent(this::setAuthentication);

        chain.doFilter(request, response);
    }

    // extract Authorization header
    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(
                ((HttpServletRequest) request)
                .getHeader("Authorization"));
    }

    // set CustomUserDetail as authenticated in Security context
    // with CustomAuthenticationToken
    private void setAuthentication(CustomUserDetails userDetails) {

        SecurityContextHolder.getContext()
                // set authentication in spring security context holder.
                // CustomAuthenticationToken inherits AbstractAuthenticationToken implements Authentication
                .setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

}
