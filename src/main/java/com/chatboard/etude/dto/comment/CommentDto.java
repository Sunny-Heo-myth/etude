package com.chatboard.etude.dto.comment;

import com.chatboard.etude.dto.member.MemberDto;
import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.helper.NestedConvertHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private MemberDto memberDto;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss",
            timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<CommentDto> children;

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                comment -> new CommentDto(
                        comment.getId(),
                        comment.isDeleted() ? null : comment.getContent(),
                        comment.isDeleted() ? null : MemberDto.toDto(comment.getMember()),
                        comment.getCreatedAt(),
                        new ArrayList<>()
                ),
                Comment::getParent,
                Comment::getId,
                CommentDto::getChildren
        );
        return helper.convert();
    }
}
