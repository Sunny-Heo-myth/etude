package com.chatboard.etude.repository.member;

import com.chatboard.etude.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    @EntityGraph("Member.roles")
    Optional<Member> findWithRolesById(Long id);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @EntityGraph("Member.roles")
    Optional<Member> findWithRolesByEmail(String email);
}
