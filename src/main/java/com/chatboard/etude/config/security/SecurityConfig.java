package com.chatboard.etude.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// filters before spring context
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .mvcMatchers(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // no session (RESTful)
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // todo httpBasic
        httpSecurity.httpBasic().disable();
        // todo formLogin
        httpSecurity.formLogin().disable();
        // todo csrf
        httpSecurity.csrf().disable();


        httpSecurity.addFilterBefore(
                // etude's JWT filter
                new JwtAuthenticationFilter(userDetailsService),
                // UsernamePasswordAuthenticationFilter is always a last filter.
                UsernamePasswordAuthenticationFilter.class
        );

        // the order of these url list DOES MATTER !!!! (specific first)
        // check authenticated
        httpSecurity.authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up", "/api/refresh-token").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/api/members/{id}/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/posts/{id}").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/posts/{id}").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/comments").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/comments/{id}").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/messages/sender", "/api/messages/receiver").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/messages/{id}").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/messages").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/messages/sender/{id}").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/messages/receiver/{id}").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/**").permitAll()

                    .antMatchers(HttpMethod.POST, "/sign", "/register", "/refresh-token").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/members/{id}/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/posts").authenticated()
                    .antMatchers(HttpMethod.PUT, "/posts/{id}").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/posts/{id}").authenticated()
                    .antMatchers(HttpMethod.POST, "/comments").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/comments/{id}").authenticated()
                    .antMatchers(HttpMethod.GET, "/messages/sender", "/messages/receiver").authenticated()
                    .antMatchers(HttpMethod.GET, "/messages/{id}").authenticated()
                    .antMatchers(HttpMethod.POST, "/messages").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/messages/sender/{id}").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/messages/receiver/{id}").authenticated()
                    .antMatchers(HttpMethod.GET, "/**").permitAll()

                    .anyRequest().hasAnyRole("ADMIN");

        // forbidden
        httpSecurity.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
        // unauthorized
        httpSecurity.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
