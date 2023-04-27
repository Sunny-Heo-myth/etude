package org.alan.etude.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.member.MemberRole;
import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.repository.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.MemberFactory.createMemberWithRoles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest    // test only with jpa related : bean registered related with jpa
@Import(QuerydslConfig.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    @PersistenceContext
    EntityManager entityManager;


    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    // test
    @Test
    void createAndReadTest() {
        // given
        Member member = createMember();

        // when
        memberRepository.save(member);
        clear();

        // then
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest() {
        // given
        Member member = createMember();

        // when
        memberRepository.save(member);
        clear();

        // then
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        assertThat(foundMember.getCreatedAt()).isNotNull();
        assertThat(foundMember.getModifiedAt()).isNotNull();
        assertThat(foundMember.getCreatedAt()).isEqualTo(foundMember.getModifiedAt());
    }

    @Test
    void updateTest() {
        // given
        String updatedNickname = "updated";
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);
        foundMember.updateNickname(updatedNickname);
        clear();

        // then
        Member updatedMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);
        assertThat(updatedMember.getNickname()).isEqualTo(updatedNickname);
    }

    @Test
    void deleteTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        memberRepository.delete(member);
        clear();

        //then
        assertThatThrownBy(() -> memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByNicknameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void uniqueEmailTest() {
        // given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember(member.getEmail(), "password2", "username2", "nickname2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void uniqueNicknameTest() {
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        // when, then
        assertThatThrownBy(() -> memberRepository.save(createMember("email2", "password2", "username2", member.getNickname())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void existsByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail() + "test")).isFalse();
    }

    @Test
    void existsByNicknameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByNickname(member.getNickname())).isTrue();
        assertThat(memberRepository.existsByNickname(member.getNickname() + "test")).isFalse();
    }

    @Test
    void memberRoleCascadePersistTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_SPECIAL_BUYER, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream()
                .map(Role::new)
                .collect(toList());

        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);
        Set<MemberRole> memberRoles = foundMember.getRoles();

        // then
        assertThat(memberRoles.size()).isEqualTo(roleTypes.size());
    }

    @Test
    void memberRoleCascadeDeleteTest() {    // Is life cycle with member and memberRole relation?
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_SPECIAL_BUYER, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream()
                .map(Role::new)
                .collect(toList());

        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<MemberRole> result = entityManager.createQuery(
                "select mr from MemberRole mr", MemberRole.class).getResultList();
        assertThat(result.size()).isZero();
    }

    @Test
    void findWithRolesByEmailTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_SPECIAL_BUYER, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream()
                .map(Role::new)
                .collect(toList());
        roleRepository.saveAll(roles);
        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        Member foundMember = memberRepository.findWithRolesByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);

        // then
        List<RoleType> result = foundMember.getRoles().stream().map(memberRole -> memberRole.getRole().getRoleType()).collect(toList());
        assertThat(result.size()).isEqualTo(roleTypes.size());
        assertThat(result).contains(roleTypes.get(0), roleTypes.get(1), roleTypes.get(2));
    }

}
