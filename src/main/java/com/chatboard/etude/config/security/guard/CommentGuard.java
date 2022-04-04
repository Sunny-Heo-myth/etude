package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.AccessDeniedException;
import com.chatboard.etude.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentGuard extends Guard {
    private final CommentRepository commentRepository;
    private final List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);


    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    protected boolean isResourceOwner(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AccessDeniedException("");
                });

        Long memberId = AuthenticationHelper.extractMemberId();
        return comment.getMember().getId().equals(memberId);
    }

}
