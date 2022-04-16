package com.chatboard.etude.dto.category;

import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;

    // todo raw type
    public static List<CategoryDto> toDtoList(List<Category> categories) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                categories,
                category -> new CategoryDto(category.getId(), category.getName(), new ArrayList<>()),
                Category::getParent,
                Category::getId,
                CategoryDto::getChildren
        );
        return helper.convert();
    }
}
