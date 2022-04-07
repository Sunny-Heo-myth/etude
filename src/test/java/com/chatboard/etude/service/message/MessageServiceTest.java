package com.chatboard.etude.service.message;

import com.chatboard.etude.dto.message.MessageCreateRequest;
import com.chatboard.etude.dto.message.MessageDto;
import com.chatboard.etude.dto.message.MessageListDto;
import com.chatboard.etude.dto.message.MessageReadCondition;
import com.chatboard.etude.entity.message.Message;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.MessageNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.message.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;


import java.util.List;
import java.util.Optional;

import static com.chatboard.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static com.chatboard.etude.factory.dto.MessageReadConditionFactory.createMessageReadCondition;
import static com.chatboard.etude.factory.entity.MemberFactory.createMember;
import static com.chatboard.etude.factory.entity.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    MessageService messageService;
    @Mock
    MessageRepository messageRepository;
    @Mock
    MemberRepository memberRepository;

    MessageReadCondition condition;
    MessageCreateRequest request;

    @Test
    void readAllBySenderTest() {
        // given
        condition = createMessageReadCondition();
        given(messageRepository.findAllBySenderIdOrderByMessageIdDesc(
                anyLong(), anyLong(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(), Pageable.ofSize(2), false));

        // when
        MessageListDto result = messageService.readAllBySender(condition);

        // then
        assertThat(result.getNumberOfElements()).isZero();
        assertThat(result.getMessageSimpleDtoList().size()).isZero();
        assertThat(result.isHasNext()).isFalse();
    }

    @Test
    void readAllByReceiverTest() {
        // given
        condition = createMessageReadCondition();
        given(messageRepository.findAllByReceiverIdOrderByMessageIdDesc(
                anyLong(), anyLong(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(), Pageable.ofSize(2), false));

        // when
        MessageListDto result = messageService.readAllByReceiver(condition);

        // then
        assertThat(result.getNumberOfElements()).isZero();
        assertThat(result.getMessageSimpleDtoList().size()).isZero();
        assertThat(result.isHasNext()).isFalse();
    }

    @Test
    void readTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        given(messageRepository.findWithSenderAndReceiverById(id)).willReturn(Optional.of(message));

        // when
        MessageDto result = messageService.read(id);

        // then
        assertThat(result.getContent()).isEqualTo(message.getContent());
    }

    @Test
    void readExceptionByMessageNotFoundTest() {
        // given
        Long id = 1L;
        given(messageRepository.findWithSenderAndReceiverById(id)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> messageService.read(id))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void createTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.of(createMember()));
        given(memberRepository.findById(request.getReceiverId())).willReturn(Optional.of(createMember()));

        // when
        messageService.create(request);

        // then
        verify(messageRepository).save(any());
    }

    @Test
    void createExceptionBySenderNotFoundTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> messageService.create(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByReceiverNotFoundTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.of(createMember()));
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> messageService.create(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteBySenderNotDeletableTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteBySender(id);

        // then
        assertThat(message.isDeletedBySender()).isTrue();
        verify(messageRepository, never()).delete(any(Message.class));
    }

    @Test
    void deleteBySenderDeletableByAlreadyReceiverDeletionTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        message.deleteByReceiver();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteBySender(id);

        // then
        assertThat(message.isDeletedBySender()).isTrue();
        verify(messageRepository).delete(any(Message.class));
    }

    @Test
    void deleteBySenderExceptionByMessageNotFoundTest() {
        // given
        Long id = 1L;
        given(messageRepository.findById(id)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> messageService.deleteBySender(id))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void deleteByReceiverNotDeletableTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteByReceiver(id);

        // then
        assertThat(message.isDeletedByReceiver()).isTrue();
        verify(messageRepository, never()).delete(any(Message.class));
    }

    @Test
    void deleteByReceiverDeletableByAlreadySenderDeletionTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        message.deleteBySender();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteByReceiver(id);

        // then
        assertThat(message.isDeletedByReceiver()).isTrue();
        verify(messageRepository).delete(any(Message.class));
    }

    @Test
    void deleteByReceiverExceptionByMessageNotFoundTest() {
        // given
        Long id = 1L;
        given(messageRepository.findById(id)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> messageService.deleteByReceiver(id))
                .isInstanceOf(MessageNotFoundException.class);
    }
}
