package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard {

    private final AuthorizationHelper authorizationHelper;

    private boolean hasAuthority(Long id) {
        Long memberId = AuthorizationHelper.extractMemberId();
        Set<RoleType> memberRoles = AuthorizationHelper.extractMemberRoles();
        return id.equals(memberId) || memberRoles.contains(RoleType.ROLE_ADMIN);
    }

    public boolean check(Long id) {
        return AuthorizationHelper.isAuthenticated()
                && hasAuthority(id);
    }
}
