package org.alan.etude.factory.dto;

import org.alan.etude.dto.post.PostCreateRequestDto;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostCreateRequestFactory {

    public static PostCreateRequestDto createPostCreateRequest() {
        return new PostCreateRequestDto("title", "content", 1000L, 1L, 1L, List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));
    }

    public static PostCreateRequestDto createPostCreateRequest(String title, String content, Long price,
                                                               Long memberId, Long categoryId, List<MultipartFile> images) {
        return new PostCreateRequestDto(title, content, price, memberId, categoryId, images);
    }

    public static PostCreateRequestDto createPostCreateRequestWithTitle(String title) {
        return new PostCreateRequestDto(title, "content", 1000L, 1L, 1L, List.of());
    }

    public static PostCreateRequestDto createPostCreateRequestWithContent(String content) {
        return new PostCreateRequestDto("title", content, 1000L, 1L, 1L, List.of());
    }

    public static PostCreateRequestDto createPostCreateRequestWithPrice(Long price) {
        return new PostCreateRequestDto("title", "content", price, 1L, 1L, List.of());
    }

    public static PostCreateRequestDto createPostCreateRequestWithMemberId(Long memberId) {
        return new PostCreateRequestDto("title", "content", 1000L, memberId, 1L, List.of());
    }

    public static PostCreateRequestDto createPostCreateRequestWithCategoryId(Long categoryId) {
        return new PostCreateRequestDto("title", "content", 1000L, 1L, categoryId, List.of());
    }

    public static PostCreateRequestDto createPostCreateRequestWithImages(List<MultipartFile> images) {
        return new PostCreateRequestDto("title", "content", 1000L, 1L, 1L, images);
    }

}
