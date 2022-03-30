package com.chatboard.etude.factory.entity;

import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;

public class RoleFactory {

    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }
}
