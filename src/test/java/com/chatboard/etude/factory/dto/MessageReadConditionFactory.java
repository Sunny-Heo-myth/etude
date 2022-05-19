package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.message.MessageReadConditionDto;

public class MessageReadConditionFactory {

    public static MessageReadConditionDto createMessageReadCondition() {
        return new MessageReadConditionDto(1L, 1L, 2);
    }

    public static MessageReadConditionDto createMessageReadCondition(
            Long memberId, Long lastMessageId, Integer size) {
        return new MessageReadConditionDto(memberId, lastMessageId, size);
    }

}
