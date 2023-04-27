package org.alan.etude.factory.entity;

import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;

public class RoleFactory {

    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }
}
