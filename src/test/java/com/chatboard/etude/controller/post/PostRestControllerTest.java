package com.chatboard.etude.controller.post;

import com.chatboard.etude.dto.post.PostCreateRequestDto;
import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import com.chatboard.etude.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.chatboard.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.chatboard.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static com.chatboard.etude.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static com.chatboard.etude.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostRestControllerTest {

    @InjectMocks
    PostRestController postRestController;
    @Mock
    PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController).build();
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                get("/api/posts/{id}", id))
                .andExpect(status().isOk());
        verify(postService).readPost(id);
    }

    @Test
    void readAllTest() throws Exception {
        // given
        PostReadConditionDto condition = createPostReadCondition(0, 10, List.of(1L, 2L), List.of(1L, 2L));

        // when, then
        mockMvc.perform(
                get("/api/posts")
                        .param("page", String.valueOf(condition.getPage()))
                        .param("size", String.valueOf(condition.getSize()))
                        .param("categoryId",
                                String.valueOf(condition.getCategoryId().get(0)),
                                String.valueOf(condition.getCategoryId().get(1)))
                        .param("memberId",
                                String.valueOf(condition.getMemberId().get(0)),
                                String.valueOf(condition.getMemberId().get(1))))
                .andExpect(status().isOk());

        verify(postService).readAllPost(condition);
    }

    @Test
    void createTest() throws Exception {
        // given : capturing PostCreateRequest received as @ModelAttribute
        ArgumentCaptor<PostCreateRequestDto> postCreateRequestArgumentCaptor =
                ArgumentCaptor.forClass(PostCreateRequestDto.class);

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        PostCreateRequestDto request = createPostCreateRequestWithImages(imageFiles);

        // when, then
        mockMvc.perform(
                multipart("/api/posts") // designate data with multipart/form-data request
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes())
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("price", String.valueOf(request.getPrice()))
                        .param("categoryId", String.valueOf(request.getCategoryId()))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("POST");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(postService).createPost(postCreateRequestArgumentCaptor.capture());  // capturing arguments

        PostCreateRequestDto capturedRequest = postCreateRequestArgumentCaptor.getValue();
        assertThat(capturedRequest.getImages().size()).isEqualTo(2);
    }

    @Test
    void updateTest() throws Exception {
        // given
        ArgumentCaptor<PostUpdateRequestDto> postUpdateRequestArgumentCaptor = ArgumentCaptor.forClass(PostUpdateRequestDto.class);

        List<MultipartFile> addedImages = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        List<Long> deletedImages = List.of(1L, 2L);

        PostUpdateRequestDto request = createPostUpdateRequest("title", "content", 1234L, addedImages, deletedImages);

        // when, then
        mockMvc.perform(
                multipart("/api/posts/{id}", 1L)
                        .file("addedImages", addedImages.get(0).getBytes())
                        .file("addedImages", addedImages.get(1).getBytes())
                        .param("deletedImages", String.valueOf(deletedImages.get(0)), String.valueOf(deletedImages.get(1)))
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("price", String.valueOf(request.getPrice()))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("PUT");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
        verify(postService).updatePost(anyLong(), postUpdateRequestArgumentCaptor.capture());

        PostUpdateRequestDto capturedRequest = postUpdateRequestArgumentCaptor.getValue();
        List<MultipartFile> capturedAddedImages = capturedRequest.getAddedImages();
        assertThat(capturedAddedImages.size()).isEqualTo(2);

        List<Long> capturedDeletedImages = capturedRequest.getDeletedImages();
        assertThat(capturedDeletedImages.size()).isEqualTo(2);
        assertThat(capturedDeletedImages).contains(deletedImages.get(0), deletedImages.get(1));
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/posts/{id}", id))
                .andExpect(status().isOk());
        verify(postService).deletePost(id);
    }
}
