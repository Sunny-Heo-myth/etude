package com.chatboard.etude.config.security;

import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.MemberRole;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    // does not load user by username but user id.
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findWithRolesById(Long.valueOf(userId))
                // for if member deleted but token is valid
                .orElseGet(() -> new Member(null, null, null, null, List.of()));

        return new CustomUserDetails(
                String.valueOf(member.getId()),
                member.getRoles().stream()
                        .map(MemberRole::getRole)
                        .map(Role::getRoleType)
                        .map(RoleType::name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
    }

//    Hibernate: select member0_.member_id as member_i1_3_0_, roles1_.member_id as member_i1_4_1_, roles1_.role_id as role_id2_4_1_, role2_.role_id as role_id1_6_2_, member0_.created_at as created_2_3_0_, member0_.modified_at as modified3_3_0_, member0_.email as email4_3_0_, member0_.nickname as nickname5_3_0_, member0_.password as password6_3_0_, member0_.username as username7_3_0_, roles1_.member_id as member_i1_4_0__, roles1_.role_id as role_id2_4_0__, role2_.role_type as role_typ2_6_2_
//    from member member0_
//    left outer join member_role roles1_ on member0_.member_id=roles1_.member_id
//    left outer join role role2_ on roles1_.role_id=role2_.role_id
//    where member0_.member_id=?
}
