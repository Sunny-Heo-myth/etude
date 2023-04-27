package org.alan.etude.service.message;

import org.alan.etude.dto.message.MessageCreateRequestDto;
import org.alan.etude.dto.message.MessageDto;
import org.alan.etude.dto.message.MessageListDto;
import org.alan.etude.dto.message.MessageReadConditionDto;
import org.alan.etude.entity.message.Message;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.MessageNotFoundException;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.message.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.alan.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.alan.etude.factory.dto.MessageReadConditionFactory.createMessageReadCondition;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.MessageFactory.createMessage;
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

    MessageReadConditionDto condition;
    MessageCreateRequestDto request;

    @Test
    void readAllBySenderTest() {
        // given
        condition = createMessageReadCondition();
        given(messageRepository.findAllMessageBySenderIdOrderByMessageIdDesc(
                anyLong(), anyLong(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(), Pageable.ofSize(2), false));

        // when
        MessageListDto result = messageService.readAllMessageBySender(condition);

        // then
        assertThat(result.getNumberOfElements()).isZero();
        assertThat(result.getMessageSimpleDtoList().size()).isZero();
        assertThat(result.isHasNext()).isFalse();
    }

    @Test
    void readAllByReceiverTest() {
        // given
        condition = createMessageReadCondition();
        given(messageRepository.findAllMessageByReceiverIdOrderByMessageIdDesc(
                anyLong(), anyLong(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(), Pageable.ofSize(2), false));

        // when
        MessageListDto result = messageService.readAllMessageByReceiver(condition);

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
        given(messageRepository.findMessageWithSenderAndReceiverById(id)).willReturn(Optional.of(message));

        // when
        MessageDto result = messageService.readAMessage(id);

        // then
        assertThat(result.getContent()).isEqualTo(message.getContent());
    }

    @Test
    void readExceptionByMessageNotFoundTest() {
        // given
        Long id = 1L;
        given(messageRepository.findMessageWithSenderAndReceiverById(id)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> messageService.readAMessage(id))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void createTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.of(createMember()));
        given(memberRepository.findById(request.getReceiverId())).willReturn(Optional.of(createMember()));

        // when
        messageService.createMessage(request);

        // then
        verify(messageRepository).save(any());
    }

    @Test
    void createExceptionBySenderNotFoundTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> messageService.createMessage(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByReceiverNotFoundTest() {
        // given
        request = createMessageCreateRequest();
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.of(createMember()));
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> messageService.createMessage(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteBySenderNotDeletableTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageBySender(id);

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
        messageService.deleteMessageBySender(id);

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
        assertThatThrownBy(() -> messageService.deleteMessageBySender(id))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void deleteByReceiverNotDeletableTest() {
        // given
        Long id = 1L;
        Message message = createMessage();
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageByReceiver(id);

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
        messageService.deleteMessageByReceiver(id);

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
        assertThatThrownBy(() -> messageService.deleteMessageByReceiver(id))
                .isInstanceOf(MessageNotFoundException.class);
    }
}
