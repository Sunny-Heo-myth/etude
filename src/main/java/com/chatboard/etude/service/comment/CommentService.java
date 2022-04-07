package com.chatboard.etude.service.comment;

import com.chatboard.etude.dto.comment.CommentCreateRequest;
import com.chatboard.etude.dto.comment.CommentDto;
import com.chatboard.etude.dto.comment.CommentReadCondition;
import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.CommentNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.repository.comment.CommentRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostNotFoundException::new);

        Comment parent = Optional.ofNullable(request.getParentId())
                // if there is parentId in request, do map() and assign parent comment instance.
                .map(parentId -> commentRepository.findById(parentId)
                        .orElseThrow(CommentNotFoundException::new))
                // or parent comment instance will remain as null. (which means this comment is root comment.)
                .orElse(null);

        Comment comment = commentRepository.save(new Comment(request.getContent(), member, post, parent));

    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment()
                .ifPresentOrElse(commentRepository::delete, comment::delete);
    }

}
