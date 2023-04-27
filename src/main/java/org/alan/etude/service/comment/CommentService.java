package org.alan.etude.service.comment;

import org.alan.etude.dto.comment.CommentCreateRequestDto;
import org.alan.etude.dto.comment.CommentDto;
import org.alan.etude.dto.comment.CommentReadConditionDto;
import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.CommentNotFoundException;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.PostNotFoundException;
import org.alan.etude.repository.comment.CommentRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    public CommentService(CommentRepository commentRepository,
                          MemberRepository memberRepository,
                          PostRepository postRepository,
                          ApplicationEventPublisher publisher) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.publisher = publisher;
    }

    public List<CommentDto> readAllCommentsHierarchical(CommentReadConditionDto condition) {
        return CommentDto.toHierarchicalDtoList(
                commentRepository.findAllCommentWithMemberAndParentByPostId(
                        condition.getPostId()
                )
        );
    }

    public List<CommentDto> readAllComments(CommentReadConditionDto condition) {
        return CommentDto.toDtoList(
                commentRepository.findAllCommentWithMemberByPostId(
                        condition.getPostId()
                )
        );
    }

    @Transactional
    public void createComment(CommentCreateRequestDto request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostNotFoundException::new);

        Comment parent = Optional.ofNullable(request.getParentId())
                .map(parentId -> commentRepository.findById(parentId)
                        .orElseThrow(CommentNotFoundException::new))
                .orElse(null);

        Comment comment = commentRepository.save(
                new Comment(request.getContent(), member, post, parent));

        // event called
        comment.publishCreatedEvent(publisher);
    }

    @Transactional
    @PreAuthorize("@commentGuard.check(#commentId)")
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findWithParentById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.findDeletableComment()
                .ifPresentOrElse(commentRepository::delete, comment::delete);
    }

    @Transactional
    @PreAuthorize("@commentGuard.check(#commentId)")
    public void updateComment(Long commentId) {

        Comment comment = commentRepository.findWithParentById(commentId)
                .orElseThrow(CommentNotFoundException::new);

    }
}
