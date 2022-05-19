package com.chatboard.etude.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "message create request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {

    @ApiModelProperty(
            value = "message",
            notes = "Enter the message.",
            required = true,
            example = "my message")
    @NotBlank(message = "{messageCreateRequest.content.notBlank}")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(
            value = "receiver id",
            notes = "Enter the receiver id.",
            example = "7")
    @NotNull(message = "{messageCreateRequest.receiverId.notNull}")
    @Positive(message = "{messageCreateRequest.receiverId.positive}")
    private Long receiverId;

}
