package com.chatboard.etude.service.comment;

import com.chatboard.etude.dto.comment.CommentCreateRequest;
import com.chatboard.etude.dto.comment.CommentDto;
import com.chatboard.etude.dto.comment.CommentReadCondition;
import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.exception.CommentNotFoundException;
import com.chatboard.etude.repository.comment.CommentRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<CommentDto> readAll(CommentReadCondition condition) {
        return CommentDto.toDtoList(
                commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(
                        condition.getPostId()
                )
        );
    }

    @Transactional
    public void create(CommentCreateRequest request) {
        commentRepository.save(CommentCreateRequest.toEntity(
                request,
                memberRepository,
                postRepository,
                commentRepository
        ));
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment()
                .ifPresentOrElse(commentRepository::delete, comment::delete);
    }
}
