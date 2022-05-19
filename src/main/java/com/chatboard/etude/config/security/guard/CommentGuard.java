package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class CommentGuard extends Guard {

    private final CommentRepository commentRepository;

    public CommentGuard(CommentRepository commentRepository) {
        super();
        this.commentRepository = commentRepository;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return commentRepository.findById(id)
                .map(Comment::getMember)
                .map(Member::getId)
                .filter(memberId -> memberId.equals(AuthUtils.extractMemberId()))
                .isPresent();
    }

}
