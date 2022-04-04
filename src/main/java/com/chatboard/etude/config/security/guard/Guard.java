package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;

import java.util.List;

public abstract class Guard {

    public final boolean check(Long id) {
        return AuthenticationHelper.isAuthenticated()
                && (hasRole(getRoleTypes()) || isResourceOwner(id));
    }

    abstract protected List<RoleType> getRoleTypes();

    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return AuthenticationHelper.extractMemberRoles().containsAll(roleTypes);
    }
}
