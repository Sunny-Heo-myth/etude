package com.chatboard.etude.controller.comment;

import com.chatboard.etude.advice.ExceptionAdvice;
import com.chatboard.etude.dto.comment.CommentCreateRequest;
import com.chatboard.etude.exception.CommentNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.handler.ResponseHandler;
import com.chatboard.etude.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.chatboard.etude.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerAdviceTest {

    @InjectMocks
    CommentController commentController;
    @Mock
    CommentService commentService;
    @Mock
    ResponseHandler responseHandler;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new ExceptionAdvice(responseHandler))
                .build();
    }

    @Test
    void createExceptionByMemberNotFoundTest() throws Exception {
        // given
        doThrow(MemberNotFoundException.class).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createExceptionByPostNotFoundTest() throws Exception {
        // given
        doThrow(PostNotFoundException.class).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createExceptionByCommentNotFoundTest() throws Exception {
        // given
        doThrow(CommentNotFoundException.class).when(commentService).create(any());
        CommentCreateRequest request = createCommentCreateRequestWithMemberId(null);

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExceptionByCommentNotFoundTest() throws Exception {
        // given
        doThrow(CommentNotFoundException.class).when(commentService).delete(anyLong());
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/comments/{id}", id))
                .andExpect(status().isNotFound());
    }
}
