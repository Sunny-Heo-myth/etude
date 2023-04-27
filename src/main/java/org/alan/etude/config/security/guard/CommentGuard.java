package org.alan.etude.config.security.guard;

import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.repository.comment.CommentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
