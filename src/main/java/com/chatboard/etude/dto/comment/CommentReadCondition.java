package com.chatboard.etude.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {
    @NotNull(message = "Enter the post number.")
    @PositiveOrZero(message = "Enter non-negative post number.")
    private Long postId;
}
