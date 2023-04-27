package org.alan.etude.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "post create request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @ApiModelProperty(value = "post title",
            notes = "Enter post title.",
            required = true,
            example = "my title")
    @NotBlank(message = "{postCreateRequest.title.notBlank}")
    private String title;

    @ApiModelProperty(value = "post content",
            notes = "Enter post content.",
            required = true,
            example = "my content")
    @NotBlank(message = "{postCreateRequest.content.notBlank}")
    private String content;

    @ApiModelProperty(value = "price",
            notes = "Enter price.",
            required = true,
            example = "50000")
    @NotNull(message = "{postCreateRequest.price.notNull}")
    @PositiveOrZero(message = "{postCreateRequest.price.positiveOrZero}")
    private Long price;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "category id",
            notes = "Enter category id.",
            required = true,
            example = "3")
    @NotNull(message = "{postCreateRequest.categoryId.notNull}")
    @PositiveOrZero(message = "{postCreateRequest.categoryId.positiveOrZero}")
    private Long categoryId;

    @ApiModelProperty(value = "image", notes = "Append image.")
    private List<MultipartFile> images = new ArrayList<>();
}
