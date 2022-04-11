package com.chatboard.etude.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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

@EnableWebSecurity
@RequiredArgsConstructor
// Activate security configuration in method.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .mvcMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.httpBasic().disable();

        httpSecurity.formLogin().disable();

        httpSecurity.csrf().disable();

        // UsernamePasswordAuthenticationFilter is always a last filter.
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);

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
                    .anyRequest().hasAnyRole("ADMIN");

        // forbidden
        httpSecurity.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
        // isUnauthorized
        httpSecurity.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
