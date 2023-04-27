package org.alan.etude.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadConditionDto {
    @NotNull(message = "Enter the post number.")
    @PositiveOrZero(message = "Enter the non-negative post number.")
    private Long postId;
}
