package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.post.PostReadCondition;

import java.util.List;

public class PostReadConditionFactory {

    public static PostReadCondition createPostReadCondition(Integer page, Integer size) {
        return new PostReadCondition(page, size, List.of(), List.of());
    }

    public static PostReadCondition createPostReadCondition(
            Integer page, Integer size, List<Long> categoryIds, List<Long> memberIds) {
        return new PostReadCondition(page, size, categoryIds, memberIds);
    }
}
