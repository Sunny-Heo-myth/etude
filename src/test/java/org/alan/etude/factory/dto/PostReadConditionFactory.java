package org.alan.etude.factory.dto;

import org.alan.etude.dto.post.PostReadConditionDto;

import java.util.List;

public class PostReadConditionFactory {

    public static PostReadConditionDto createPostReadCondition(Integer page, Integer size) {
        return new PostReadConditionDto(page, size, "keyword", List.of(), List.of());
    }

    public static PostReadConditionDto createPostReadCondition(
            Integer page, Integer size, List<Long> categoryIds, List<Long> memberIds) {
        return new PostReadConditionDto(page, size, "keyword", categoryIds, memberIds);
    }
}
