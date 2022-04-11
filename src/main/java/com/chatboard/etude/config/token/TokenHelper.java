package com.chatboard.etude.config.token;

import com.chatboard.etude.handler.JwtHandler;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
// helper is not main "service" which this application serves.
// JwtHandler <-> TokenHelper <-> SignService
//                            <-> UserDetailService
public class TokenHelper {

    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    private static final String SEP =",";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String MEMBER_ID = "MEMBER_ID";

    // When create token, PrivateClaims(only for id and role) will be passed.
    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.getMemberId(),
                        // multiple roles will be saved in a single string.
                        ROLE_TYPES, String.join(SEP, privateClaims.getRoleTypes())
                ),
                maxAgeSeconds);
    }

    public Optional<PrivateClaims> parse(String token) {
        return jwtHandler.parse(key, token).map(this::convert);
    }

    // convert jwtClaim into Private(Custom)Claim for Etude
    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String memberId;
        private List<String> roleTypes;
    }

}
