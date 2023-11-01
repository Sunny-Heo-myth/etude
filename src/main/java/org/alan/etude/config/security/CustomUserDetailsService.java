package org.alan.etude.config.security;

import lombok.RequiredArgsConstructor;
import org.alan.etude.config.token.TokenHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TokenHelper accessTokenHelper;

    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return accessTokenHelper.parse(token)
                .map(this::convert)
                .orElse(null);
    }

    // payload to userDetail
    private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.memberId(),
                privateClaims.roleTypes().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
    }

}
