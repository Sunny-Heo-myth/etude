package com.chatboard.etude.config.token;

import com.chatboard.etude.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    private final JwtHandler jwtHandler;

    public TokenConfig(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Bean
    public TokenHelper accessTokenHelper(@Value("${jwt.key.access}") String key,
                                         @Value("${jwt.max-age.access}") long maxAgeSeconds) {
        return new TokenHelper(jwtHandler, key, maxAgeSeconds);
    }

    @Bean
    public TokenHelper refreshTokenHelper(@Value("${jwt.key.refresh}") String key,
                                          @Value("${jwt.max-age.refresh}") long maxAgeSeconds) {
        return new TokenHelper(jwtHandler, key, maxAgeSeconds);
    }

}
