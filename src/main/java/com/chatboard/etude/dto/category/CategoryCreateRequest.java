package com.chatboard.etude.dto.category;

import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@ApiModel(value = "category create request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @ApiModelProperty(value = "category name",
            notes = "enter the category name",
            required = true,
            example = "my category")
    @NotBlank(message = "{categoryCreateRequest.name.notBlank}")
    @Size(min = 2, max = 30, message = "{categoryCreateRequest.name.size}")
    private String name;

    @ApiModelProperty(value = "parent category id",
            notes = "enter the parent category id",
            example = "7")
    private Long parentId;

}
