package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;

import java.util.List;

public abstract class Guard {

    // authentication and authorization is not our main service
    // both auth-strategies are not business logic and will not be the part of service logics.
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<RoleType> getRoleTypes();

    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return AuthHelper.extractMemberRoles().containsAll(roleTypes);
    }
}
