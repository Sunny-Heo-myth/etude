package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.config.security.CustomAuthenticationToken;
import com.chatboard.etude.config.security.CustomUserDetails;
import com.chatboard.etude.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthorizationHelper {

    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        return getAuthentication() instanceof CustomAuthenticationToken
                && getAuthentication().isAuthenticated();
    }

    public static Long extractMemberId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public static Set<RoleType> extractMemberRoles() {
        return getUserDetails().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());
    }

}
