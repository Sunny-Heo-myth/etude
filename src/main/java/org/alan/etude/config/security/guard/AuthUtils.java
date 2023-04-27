package org.alan.etude.config.security.guard;

import lombok.NoArgsConstructor;
import org.alan.etude.config.security.CustomAuthenticationToken;
import org.alan.etude.config.security.CustomUserDetails;
import org.alan.etude.entity.member.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthUtils {

    public static boolean isAuthenticated() {
        return getAuthentication() instanceof
                CustomAuthenticationToken
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

    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
