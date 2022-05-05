package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.post.PostReadConditionDto;

import java.util.List;

public class PostReadConditionFactory {

    public static PostReadConditionDto createPostReadCondition(Integer page, Integer size) {
        return new PostReadConditionDto(page, size, List.of(), List.of());
    }

    public static PostReadConditionDto createPostReadCondition(
            Integer page, Integer size, List<Long> categoryIds, List<Long> memberIds) {
        return new PostReadConditionDto(page, size, categoryIds, memberIds);
    }
}
