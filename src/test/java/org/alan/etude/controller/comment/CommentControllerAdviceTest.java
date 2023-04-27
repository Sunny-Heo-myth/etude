package org.alan.etude.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.advice.ExceptionAdvice;
import org.alan.etude.dto.comment.CommentCreateRequestDto;
import org.alan.etude.exception.notFoundException.CommentNotFoundException;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.PostNotFoundException;
import org.alan.etude.handler.FailureResponseHandler;
import org.alan.etude.service.comment.CommentService;
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

import static org.alan.etude.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerAdviceTest {

    @InjectMocks
    CommentRestController commentRestController;
    @Mock
    CommentService commentService;
    @Mock
    FailureResponseHandler failureResponseHandler;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(commentRestController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void createExceptionByMemberNotFoundTest() throws Exception {
        // given
        doThrow(MemberNotFoundException.class).when(commentService).createComment(any());
        CommentCreateRequestDto request = createCommentCreateRequestWithMemberId(null);

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
        doThrow(PostNotFoundException.class).when(commentService).createComment(any());
        CommentCreateRequestDto request = createCommentCreateRequestWithMemberId(null);

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
        doThrow(CommentNotFoundException.class).when(commentService).createComment(any());
        CommentCreateRequestDto request = createCommentCreateRequestWithMemberId(null);

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
        doThrow(CommentNotFoundException.class).when(commentService).deleteComment(anyLong());
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/comments/{id}", id))
                .andExpect(status().isNotFound());
    }
}
