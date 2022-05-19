package com.chatboard.etude.controller.comment;

import com.chatboard.etude.dto.comment.CommentCreateRequestDto;
import com.chatboard.etude.dto.comment.CommentReadConditionDto;
import com.chatboard.etude.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.chatboard.etude.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static com.chatboard.etude.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    @InjectMocks
    CommentRestController commentRestController;
    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentRestController)
                .build();
    }

    @Test
    void readAllTest() throws Exception {
        // given
        CommentReadConditionDto condition = createCommentReadCondition();

        // when, then
        mockMvc.perform(
                get("/api/comments")
                        .param("postId", String.valueOf(condition.getPostId())))
                .andExpect(status().isOk());
    }

    @Test
    void createTest() throws Exception {
        // given
        CommentCreateRequestDto request = createCommentCreateRequestWithMemberId(null);

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(commentService).createComment(request);
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/comments/{id}", id))
                .andExpect(status().isOk());

        verify(commentService).deleteComment(id);
    }
}
