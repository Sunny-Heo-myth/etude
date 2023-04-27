package org.alan.etude.config.security.guard;

import org.alan.etude.entity.member.RoleType;

import java.util.List;

// authentication and authorization is not our main service
// both auth-strategies are not business logic and will not be the part of service logics.
public abstract class Guard {

    private final List<RoleType> roleTypes;

    public Guard() {
        this.roleTypes = List.of(RoleType.ROLE_ADMIN);
    }

    // Authentication check must come first since accessing db is much slower.
    // Guard does not need to check authenticated anymore.
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    protected List<RoleType> getRoleTypes() {
        return this.roleTypes;
    }

    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return AuthUtils.extractMemberRoles().containsAll(roleTypes);
    }
}
