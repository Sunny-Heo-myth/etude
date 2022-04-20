package com.chatboard.etude.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

// Client modify the images of a post.
// New image will be sent as MultiPartFile
// Old image is found and deleted by id
@ApiModel(value = "post update request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

    @ApiModelProperty(value = "post title",
            notes = "Enter post title.",
            required = true,
            example = "my title")
    @NotBlank(message = "{postUpdateRequest.title.notBlank}")
    private String title;

    @ApiModelProperty(value = "post content",
            notes = "Enter post content.",
            required = true,
            example = "my content")
    @NotBlank(message = "{postUpdateRequest.content.notBlank}")
    private String content;

    @ApiModelProperty(value = "price", notes = "Enter the price.", required = true, example = "50000")
    @NotNull(message = "{postUpdateRequest.price.notNull}")
    @PositiveOrZero(message = "{postUpdateRequest.price.positiveOrZero}")
    private Long price;

    // Send whole file of image when add image to post.
    @ApiModelProperty(value = "added image", notes = "Attach added image.")
    private List<MultipartFile> addedImages = new ArrayList<>();

    // Send only id of image when delete image from post.
    @ApiModelProperty(value = "deleted image id.", notes = "Enter the id of deleted image.")
    private List<Long> deletedImages = new ArrayList<>();
}
