package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.entity.message.Message;
import com.chatboard.etude.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class MessageGuard extends Guard{

    private final MessageRepository messageRepository;

    public MessageGuard(MessageRepository messageRepository) {
        super();
        this.messageRepository = messageRepository;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return messageRepository.findById(id)
                .map(Message::getSender)
                .map(Member::getId)
                .filter(senderId -> senderId.equals(AuthUtils.extractMemberId()))
                .isPresent();
    }
}
