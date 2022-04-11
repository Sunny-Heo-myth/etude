package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;

import java.util.List;

// authentication and authorization is not our main service
// both auth-strategies are not business logic and will not be the part of service logics.
public abstract class Guard {
    // Authentication check must come first since accessing db is much slower.
    // Guard does not need to check authenticated anymore.
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<RoleType> getRoleTypes();

    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return AuthUtils.extractMemberRoles().containsAll(roleTypes);
    }
}
