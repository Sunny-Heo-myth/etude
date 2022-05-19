package com.chatboard.etude.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiOperation(value = "comment create request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {

    @ApiModelProperty(value = "comment", notes = "Enter comment.", required = true, example = "my comment")
    @NotBlank(message = "{commentCreateRequest.content.notBlank}")
    private String content;

    @ApiModelProperty(value = "post id", notes = "Enter post id.", example = "7")
    @NotNull(message = "{commentCreateRequest.postId.notNull}")
    @Positive(message = "{commentCreateRequest.postId.positive}")
    private Long postId;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "parent comment id", notes = "Enter parent comment id.", example = "7")
    private Long parentId;

}
