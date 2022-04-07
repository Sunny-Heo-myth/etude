package com.chatboard.etude.dto.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReadCondition {

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(
            value = "last message id",
            notes = "Enter the id of last found message.",
            required = true,
            example = "7")
    private Long LastMessageId = Long.MAX_VALUE;

    @ApiModelProperty(
            value = "page size",
            notes = "Enter page size.",
            required = true,
            example = "10")
    @NotNull(message = "Enter page size.")
    @Positive(message = "Enter the positive page size.")
    private Integer size;
}
