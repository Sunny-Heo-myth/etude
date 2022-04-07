package com.chatboard.etude.service.comment;

import com.chatboard.etude.dto.comment.CommentDto;
import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.event.comment.CommentCreatedEvent;
import com.chatboard.etude.exception.CommentNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.repository.comment.CommentRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import com.chatboard.etude.service.comment.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static com.chatboard.etude.factory.dto.CommentCreateRequestFactory.*;
import static com.chatboard.etude.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static com.chatboard.etude.factory.entity.CommentFactory.createComment;
import static com.chatboard.etude.factory.entity.CommentFactory.createDeletedComment;
import static com.chatboard.etude.factory.entity.MemberFactory.createMember;
import static com.chatboard.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @Test
    void readAllTest() {
        // given
        given(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(anyLong()))
                .willReturn(
                        List.of(createComment(null), createComment(null))
                );

        // when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void readAllDeletedCommentTest() {
        // given
        given(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(anyLong()))
                .willReturn(
                        List.of(createDeletedComment(null), createDeletedComment(null))
                );

        // when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isNull();
        assertThat(result.get(0).getMemberDto()).isNull();
    }

    @Test
    void createTest() {
        // given
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));
        given(commentRepository.save(any())).willReturn(createComment(null));

        // when
        commentService.create(createCommentCreateRequest());

        // then
        verify(commentRepository).save(any());
        verify(publisher).publishEvent(eventCaptor.capture());

        Object event = eventCaptor.getValue();
        assertThat(event).isInstanceOf(CommentCreatedEvent.class);
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByPostNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void createExceptionByCommentNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequestWithParentId(1L)))
                .isInstanceOf(CommentNotFoundException.class);
    }
}
