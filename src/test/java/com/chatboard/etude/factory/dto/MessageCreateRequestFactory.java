package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.message.MessageCreateRequestDto;

public class MessageCreateRequestFactory {

    public static MessageCreateRequestDto createMessageCreateRequest() {
        return new MessageCreateRequestDto("content", 1L, 2L);
    }

    public static MessageCreateRequestDto createMessageCreateRequest(
            String content, Long memberId, Long receiverId) {
        return new MessageCreateRequestDto(content, memberId, receiverId);
    }

}
