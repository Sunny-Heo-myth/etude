package com.chatboard.etude.repository.role;

import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
