package com.chatboard.etude.config.security;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.MemberRole;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final TokenHelper accessTokenHelper;

    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return accessTokenHelper.parse(token)
                .map(this::convert)
                .orElse(null);
    }

    private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.getMemberId(),
                privateClaims.getRoleTypes().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
    }

    //    private final MemberRepository memberRepository;
//
//    @Override
//    // **does not load user by username but user id.
//    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        // If there is no effect for user by using old token, we do not need to access database for every request.
//        // Or we can save UserDetails in speedy memory DB.
//        // though, in this project, there is no API which changes subject of a token.
//        Member member = memberRepository.findWithRolesById(Long.valueOf(userId))
//                // when member is deleted already but the issued token is still valid
//                .orElseGet(() -> new Member(null, null, null, null, List.of()));
//
//        return new CustomUserDetails(
//                String.valueOf(member.getId()),
//                member.getRoles().stream()
//                        .map(MemberRole::getRole)
//                        .map(Role::getRoleType)
//                        .map(RoleType::name)
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toSet())
//        );
//    }

}
