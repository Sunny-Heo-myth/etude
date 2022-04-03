package com.chatboard.etude.controller.post;

import com.chatboard.etude.advice.ExceptionAdvice;
import com.chatboard.etude.dto.post.PostCreateRequest;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.exception.UnsupportedImageFormatException;
import com.chatboard.etude.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.chatboard.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerAdviceTest {
    @InjectMocks
    PostController postController;
    @Mock
    PostService postService;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new ExceptionAdvice())
                .build();
    }

    @Test
    void createExceptionByMemberNotFoundException() throws Exception {
        // given
        given(postService.create(any())).willThrow(MemberNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1007));

    }

    @Test
    void createExceptionByCategoryNotFoundException() throws Exception {
        // given
        given(postService.create(any())).willThrow(CategoryNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1010));

    }

    @Test
    void createExceptionByUnsupportedImageNotFoundException() throws Exception {
        // given
        given(postService.create(any())).willThrow(UnsupportedImageFormatException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1013));

    }

    private ResultActions performCreate() throws Exception {
        PostCreateRequest request = createPostCreateRequest();
        return mockMvc.perform(
                multipart("/api/posts")
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("price", String.valueOf(request.getPrice()))
                        .param("categoryId", String.valueOf(request.getCategoryId()))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("POST");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    @Test
    void readExceptionByPostNotFoundTest() throws Exception {
        // given
        given(postService.read(anyLong())).willThrow(PostNotFoundException.class);

        // when, then
        mockMvc.perform(
                get("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1012));
    }

    @Test
    void updateExceptionByPostNotFoundTest() throws Exception {
        // given
        given(postService.update(anyLong(), any())).willThrow(PostNotFoundException.class);

        // when, then
        mockMvc.perform(
                        multipart("/api/posts/{id}", 1L)
                                .param("title", "title")
                                .param("content", "content")
                                .param("price", "1234")
                                .with(requestProcessor -> {
                                    requestProcessor.setMethod("PUT");
                                    return requestProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1012));
    }

    @Test
    void deleteExceptionByPostNotFoundTest() throws Exception {
        // given
        doThrow(PostNotFoundException.class).when(postService).delete(anyLong());

        // when, then
        mockMvc.perform(
                delete("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1012));
    }

}