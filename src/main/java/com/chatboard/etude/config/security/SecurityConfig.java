package com.chatboard.etude.config.security;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.handler.CustomAccessDeniedHandler;
import com.chatboard.etude.handler.CustomAuthenticationEntryPoint;
import com.chatboard.etude.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring()
                .mvcMatchers("/exception/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up", "/api/refresh-token").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/api/members/{id}/**").access("@memberGuard.check(#id)")  // .authenticated()
                        .antMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                        .antMatchers(HttpMethod.PUT, "/api/posts/{id}").access("@postGuard.check(#id)")
                        .antMatchers(HttpMethod.DELETE, "/api/posts/{id}").access("@postGuard.check(#id)")
                        .anyRequest().hasAnyRole("ADMIN")

                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(accessTokenHelper, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
