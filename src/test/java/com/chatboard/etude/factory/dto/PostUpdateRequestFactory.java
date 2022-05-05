package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostUpdateRequestFactory {
    public static PostUpdateRequestDto createPostUpdateRequest(String title,
                                                               String content,
                                                               Long price,
                                                               List<MultipartFile> addedImages,
                                                               List<Long> deletedImages) {
        return new PostUpdateRequestDto(title, content, price, addedImages, deletedImages);
    }
}
