package org.alan.etude.dto.message;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReadConditionDto {

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(
            value = "last message id",
            notes = "Enter the id of last found message.",
            required = true,
            example = "7")
    private Long lastMessageId = Long.MAX_VALUE;

    @ApiModelProperty(
            value = "page size",
            notes = "Enter page size.",
            required = true,
            example = "10")
    @NotNull(message = "Enter page size.")
    @Positive(message = "Enter the positive page size.")
    private Integer size;
}
