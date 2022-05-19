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
