package org.alan.etude.entity.post;

import org.alan.etude.dto.post.PostUpdateRequestDto;
import org.alan.etude.factory.dto.PostUpdateRequestFactory;
import org.alan.etude.factory.entity.CategoryFactory;
import org.alan.etude.factory.entity.ImageFactory;
import org.alan.etude.factory.entity.MemberFactory;
import org.alan.etude.factory.entity.PostFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    void updateTest() {
        // given
        Image oldImage1 = ImageFactory.createImageWithIdAndOriginName(1L, "oldImage1.jpg");
        Image oldImage2 = ImageFactory.createImageWithIdAndOriginName(2L, "oldImage2.jpg");
        Post post = PostFactory.createPostWithImages(MemberFactory.createMember(), CategoryFactory.createCategory(), List.of(oldImage1, oldImage2));

        // when
        MockMultipartFile newImageFile = new MockMultipartFile(
                "file1", "file1.png", MediaType.IMAGE_PNG_VALUE, "file1InputStream".getBytes());

        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestFactory.createPostUpdateRequest(
                "update title", "update content", 1234L, List.of(newImageFile), List.of(oldImage1.getId()));

        Post.ImageUpdatedResult imageUpdatedResult = post.update(postUpdateRequestDto);

        // then
        assertThat(post.getTitle()).isEqualTo(postUpdateRequestDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateRequestDto.getContent());
        assertThat(post.getPrice()).isEqualTo(postUpdateRequestDto.getPrice());

        List<Image> resultImages = post.getImages();
        List<String> resultOriginNames = resultImages.stream()
                .map(Image::getOriginName)
                .collect(Collectors.toList());
        assertThat(resultImages.size()).isEqualTo(2);
        assertThat(resultOriginNames).contains(oldImage2.getOriginName(), newImageFile.getOriginalFilename());

        List<MultipartFile> addedImageFiles = imageUpdatedResult.getAddedImageFiles();
        assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedImageFiles.get(0).getOriginalFilename()).isEqualTo(newImageFile.getOriginalFilename());

        List<Image> addedImages = imageUpdatedResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream()
                .map(Image::getOriginName)
                .collect(Collectors.toList());
        assertThat(addedImages.size()).isEqualTo(1);
        assertThat(addedOriginNames).contains(newImageFile.getOriginalFilename());

        List<Image> deletedImages = imageUpdatedResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream()
                .map(Image::getOriginName)
                .collect(Collectors.toList());
        assertThat(deletedImages.size()).isEqualTo(1);
        assertThat(deletedOriginNames).contains(oldImage1.getOriginName());
    }
}
