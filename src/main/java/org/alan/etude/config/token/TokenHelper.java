package org.alan.etude.config.token;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.alan.etude.handler.JwtHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// helper is not main "service" which this application serves.
// JwtHandler <-> TokenHelper <-> SignService
//                            <-> UserDetailService
public class TokenHelper {

    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    public TokenHelper(JwtHandler jwtHandler, String key, long maxAgeSeconds) {
        this.jwtHandler = jwtHandler;
        this.key = key;
        this.maxAgeSeconds = maxAgeSeconds;
    }

    private static final String SEPARATION =",";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String MEMBER_ID = "MEMBER_ID";

    // When create token, PrivateClaims(only for id and role) will be passed.
    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.memberId(),
                        // multiple roles will be saved as a single string.
                        ROLE_TYPES, String.join(SEPARATION, privateClaims.roleTypes())
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
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEPARATION))
        );
    }

    // claims which converted as payload of JWT
        @Getter
        public record PrivateClaims(String memberId, List<String> roleTypes) {

    }

}
