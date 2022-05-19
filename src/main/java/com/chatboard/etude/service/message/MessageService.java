package com.chatboard.etude.service.message;

import com.chatboard.etude.dto.message.MessageCreateRequestDto;
import com.chatboard.etude.dto.message.MessageDto;
import com.chatboard.etude.dto.message.MessageListDto;
import com.chatboard.etude.dto.message.MessageReadConditionDto;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.message.Message;
import com.chatboard.etude.exception.notFoundException.MemberNotFoundException;
import com.chatboard.etude.exception.notFoundException.MessageNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.message.MessageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageService(MessageRepository messageRepository, MemberRepository memberRepository) {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    public MessageListDto readAllMessageBySender(MessageReadConditionDto condition) {
        return MessageListDto.toDto(messageRepository.findAllBySenderIdOrderByMessageIdDesc(
                condition.getMemberId(), condition.getLastMessageId(), Pageable.ofSize(condition.getSize())
        ));
    }

    public MessageListDto readAllMessageByReceiver(MessageReadConditionDto condition) {
        return MessageListDto.toDto(messageRepository.findAllByReceiverIdOrderByMessageIdDesc(
                condition.getMemberId(), condition.getLastMessageId(), Pageable.ofSize(condition.getSize())
        ));
    }

    @PreAuthorize("@messageGuard.check(#id)")
    public MessageDto readAMessage(Long id) {
        return MessageDto.toDto(
                messageRepository.findWithSenderAndReceiverById(id)
                        .orElseThrow(MessageNotFoundException::new)
        );
    }

    @Transactional
    public void createMessage(MessageCreateRequestDto request) {

        Member sender = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Member receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(MemberNotFoundException::new);

        Message message = new Message(request.getContent(), sender, receiver);

        messageRepository.save(message);
    }

    @Transactional
    @PreAuthorize("@messageSenderGuard.check(#id)")
    public void deleteMessageBySender(Long id) {
        deleteMessage(id, Message::deleteBySender);
    }

    @Transactional
    @PreAuthorize("@messageReceiverGuard.check(#id)")
    public void deleteMessageByReceiver(Long id) {
        deleteMessage(id, Message::deleteByReceiver);
    }

    private void deleteMessage(Long id, Consumer<Message> delete) {

        Message message = messageRepository.findById(id)
                .orElseThrow(MessageNotFoundException::new);

        delete.accept(message);

        if (message.isDeletable()) {
            messageRepository.delete(message);
        }
    }
}
