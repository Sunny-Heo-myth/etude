package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.comment.CommentReadConditionDto;

public class CommentReadConditionFactory {

    public static CommentReadConditionDto createCommentReadCondition() {
        return new CommentReadConditionDto(1L);
    }

    public static CommentReadConditionDto createCommentReadCondition(Long postId) {
        return new CommentReadConditionDto(postId);
    }
}
