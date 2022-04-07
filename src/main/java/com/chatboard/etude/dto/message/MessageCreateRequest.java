package com.chatboard.etude.dto.message;

import com.chatboard.etude.entity.message.Message;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
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
public class MessageCreateRequest {

    @ApiModelProperty(
            value = "message",
            notes = "Enter the message.",
            required = true,
            example = "my message")
    @NotBlank(message = "Enter the message")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(
            value = "receiver id",
            notes = "Enter the receiver id.",
            example = "7")
    @NotNull(message = "Enter the receiver id.")
    @Positive(message = "Enter the positive receiver id.")
    private Long receiverId;

}
