package org.alan.etude.service.post;


import org.alan.etude.dto.post.PostCreateRequestDto;
import org.alan.etude.dto.post.PostDto;
import org.alan.etude.dto.post.PostListDto;
import org.alan.etude.dto.post.PostUpdateRequestDto;
import org.alan.etude.entity.post.Image;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.UnsupportedImageFormatException;
import org.alan.etude.exception.notFoundException.CategoryNotFoundException;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.PostNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.alan.etude.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.alan.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static org.alan.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static org.alan.etude.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.alan.etude.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.alan.etude.factory.entity.CategoryFactory.createCategory;
import static org.alan.etude.factory.entity.ImageFactory.createImage;
import static org.alan.etude.factory.entity.ImageFactory.createImageWithIdAndOriginName;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @Test
    void createTest() {
        // given
        PostCreateRequestDto request = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0, request.getImages().size())
                        .mapToObj(num -> createImage())
                        .collect(Collectors.toList())
        ));

        // when
        postService.createPost(request);

        // then
        verify(postRepository).save(any());
        verify(fileService, times(request.getImages().size())).upload(any(), anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.createPost(createPostCreateRequest()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByCategoryNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());


        // when, then
        assertThatThrownBy(() -> postService.createPost(createPostCreateRequest()))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest() {
        // given
        PostCreateRequestDto request = createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test",
                        "test.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when, then
        assertThatThrownBy(() -> postService.createPost(request))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void readTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        PostDto postDto = postService.readPost(1L);

        // then
        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
    }

    @Test
    void readAllTest() {
        // given
        given(postRepository.findAllByCondition(any())).willReturn(Page.empty());

        // when
        PostListDto postListDto = postService.readAllPost(createPostReadCondition(1, 1));

        // then
        assertThat(postListDto.getPostSimpleDtos().size()).isZero();
    }
    @Test
    void readExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.readPost(1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        verify(fileService, times(post.getImages().size())).delete(anyString());
        verify(postRepository).delete(any());
    }

    @Test
    void deleteExceptionByNotFoundPostTest() {
        // given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.deletePost(1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void updateTest() {
        // given
        Image image1 = createImageWithIdAndOriginName(1L, "image1.png");
        Image image2 = createImageWithIdAndOriginName(2L, "image2.png");
        Post post = createPostWithImages(List.of(image1, image2));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        MockMultipartFile image3File = new MockMultipartFile("image3", "image3.png", MediaType.IMAGE_PNG_VALUE, "image3".getBytes());
        PostUpdateRequestDto postUpdateRequestDto = createPostUpdateRequest("title", "content", 1000L, List.of(image3File), List.of(image1.getId()));

        // when
        postService.updatePost(1L, postUpdateRequestDto);

        // then
        List<Image> images = post.getImages();
        List<String> originNames = images.stream()
                .map(Image::getOriginName)
                .collect(Collectors.toList());
        assertThat(originNames.size()).isEqualTo(2);
        assertThat(originNames).contains(image2.getOriginName(), image3File.getOriginalFilename());

        verify(fileService, times(1)).upload(any(), anyString());
        verify(fileService, times(1)).delete(anyString());
    }

    @Test
    void updateExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.updatePost(1L, createPostUpdateRequest("title", "content", 1234L, List.of(), List.of())))
                .isInstanceOf(PostNotFoundException.class);
    }

}