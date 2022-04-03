package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.AccessDeniedException;
import com.chatboard.etude.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard {
    private final AuthenticationHelper authenticationHelper;
    private final PostRepository postRepository;

    public boolean check(Long id) {
        return AuthenticationHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id);
    }

    private boolean hasAdminRole() {
        return AuthenticationHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }

    private boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AccessDeniedException("");
                });
        Long memberId = AuthenticationHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }

}
