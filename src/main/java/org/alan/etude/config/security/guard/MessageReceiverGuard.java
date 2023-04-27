package org.alan.etude.config.security.guard;

import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.message.Message;
import org.alan.etude.repository.message.MessageRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MessageReceiverGuard extends Guard{

    private final MessageRepository messageRepository;

    public MessageReceiverGuard(MessageRepository messageRepository) {
        super();
        this.messageRepository = messageRepository;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return messageRepository.findById(id)
                .map(Message::getReceiver)
                .map(Member::getId)
                .filter(receiverId -> receiverId.equals(AuthUtils.extractMemberId()))
                .isPresent();
    }

}
