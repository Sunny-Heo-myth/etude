package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard extends Guard{

    private final List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthUtils.extractMemberId());
    }
}
