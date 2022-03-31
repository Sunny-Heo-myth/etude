package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.category.CategoryCreateRequest;

public class CategoryCreateRequestFactory {

    public static CategoryCreateRequest createCategoryCreateRequest() {
        return new CategoryCreateRequest("category", null);
    }

    public static CategoryCreateRequest createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequest(name, null);
    }
}
