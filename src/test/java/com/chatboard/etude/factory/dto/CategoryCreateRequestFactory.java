package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.category.CategoryCreateRequestDto;

public class CategoryCreateRequestFactory {

    public static CategoryCreateRequestDto createCategoryCreateRequest() {
        return new CategoryCreateRequestDto("category", null);
    }

    public static CategoryCreateRequestDto createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequestDto(name, null);
    }
}
