package com.chatboard.etude.config.security;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.service.sign.TokenService;
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

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    // Only when access token is valid, save user info in context.
    // verifying refresh token & read user info function deleted.

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader("Authorization");
    }

    private boolean validateToken(String token) {
        return token != null && accessTokenHelper.validate(token);
    }

    private void setAuthentication(String token) {
        String userId = accessTokenHelper.extractSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext()
                .setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if (validateToken(token)) {
            setAuthentication(token);
        }
        chain.doFilter(request, response);
    }
}
