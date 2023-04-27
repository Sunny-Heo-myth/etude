package org.alan.etude.config.security.guard;

import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.message.Message;
import org.alan.etude.repository.message.MessageRepository;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderGuard extends Guard{

    private final MessageRepository messageRepository;

    public MessageSenderGuard(MessageRepository messageRepository) {
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
