package org.alan.etude.factory.dto;

import org.alan.etude.dto.comment.CommentCreateRequestDto;

public class CommentCreateRequestFactory {

    public static CommentCreateRequestDto createCommentCreateRequest() {
        return new CommentCreateRequestDto("content", 1L, 1L, null);
    }

    public static CommentCreateRequestDto createCommentCreateRequest(String content, Long postId, Long memberId, Long parentId) {
        return new CommentCreateRequestDto(content, postId, memberId, parentId);
    }

    public static CommentCreateRequestDto createCommentCreateRequestWithContent(String content) {
        return new CommentCreateRequestDto(content, 1L, 1L, null);
    }

    public static CommentCreateRequestDto createCommentCreateRequestWithPostId(Long postId) {
        return new CommentCreateRequestDto("content", postId, 1L, null);
    }

    public static CommentCreateRequestDto createCommentCreateRequestWithMemberId(Long memberId) {
        return new CommentCreateRequestDto("content", 1L, memberId, null);
    }

    public static CommentCreateRequestDto createCommentCreateRequestWithParentId(Long parentId) {
        return new CommentCreateRequestDto("content", 1L, 1L, parentId);
    }
}
