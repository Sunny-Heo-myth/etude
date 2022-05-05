package com.chatboard.etude.controller.post;

import com.chatboard.etude.advice.ExceptionAdvice;
import com.chatboard.etude.dto.post.PostCreateRequestDto;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.exception.UnsupportedImageFormatException;
import com.chatboard.etude.handler.FailureResponseHandler;
import com.chatboard.etude.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
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
public class PostRestControllerAdviceTest {
    @InjectMocks
    PostRestController postRestController;
    @Mock
    PostService postService;
    @Mock
    FailureResponseHandler failureResponseHandler;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(postRestController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void createExceptionByMemberNotFoundException() throws Exception {
        // given
        given(postService.createPost(any())).willThrow(MemberNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());

    }

    @Test
    void createExceptionByCategoryNotFoundException() throws Exception {
        // given
        given(postService.createPost(any())).willThrow(CategoryNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());

    }

    @Test
    void createExceptionByUnsupportedImageNotFoundException() throws Exception {
        // given
        given(postService.createPost(any())).willThrow(UnsupportedImageFormatException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());

    }

    @Test
    void readExceptionByPostNotFoundTest() throws Exception {
        // given
        given(postService.readPost(anyLong())).willThrow(PostNotFoundException.class);

        // when, then
        mockMvc.perform(
                get("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateExceptionByPostNotFoundTest() throws Exception {
        // given
        given(postService.updatePost(anyLong(), any())).willThrow(PostNotFoundException.class);

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
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExceptionByPostNotFoundTest() throws Exception {
        // given
        doThrow(PostNotFoundException.class).when(postService).deletePost(anyLong());

        // when, then
        mockMvc.perform(
                delete("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    private ResultActions performCreate() throws Exception {
        PostCreateRequestDto request = createPostCreateRequest();
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

}
