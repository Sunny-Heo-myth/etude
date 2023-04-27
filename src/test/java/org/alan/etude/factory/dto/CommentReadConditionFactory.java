package org.alan.etude.factory.dto;

import org.alan.etude.dto.comment.CommentReadConditionDto;

public class CommentReadConditionFactory {

    public static CommentReadConditionDto createCommentReadCondition() {
        return new CommentReadConditionDto(1L);
    }

    public static CommentReadConditionDto createCommentReadCondition(Long postId) {
        return new CommentReadConditionDto(postId);
    }
}
