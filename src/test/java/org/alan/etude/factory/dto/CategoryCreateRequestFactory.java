package org.alan.etude.factory.dto;

import org.alan.etude.dto.category.CategoryCreateRequestDto;

public class CategoryCreateRequestFactory {

    public static CategoryCreateRequestDto createCategoryCreateRequest() {
        return new CategoryCreateRequestDto("category", null);
    }

    public static CategoryCreateRequestDto createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequestDto(name, null);
    }
}
