package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.entity.message.Message;
import com.chatboard.etude.exception.AccessDeniedException;
import com.chatboard.etude.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageReceiverGuard extends Guard{

    private final MessageRepository messageRepository;
    private final List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AccessDeniedException("");
                });

        return message.getReceiver().getId().equals(AuthenticationHelper.extractMemberId());
    }
}
