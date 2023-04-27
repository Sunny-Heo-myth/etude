package org.alan.etude.repository.role;

import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
