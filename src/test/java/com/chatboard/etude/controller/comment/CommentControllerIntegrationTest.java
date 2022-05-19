package com.chatboard.etude.controller.comment;

import com.chatboard.etude.dto.comment.CommentCreateRequestDto;
import com.chatboard.etude.dto.comment.CommentDto;
import com.chatboard.etude.dto.sign.SignInResponseDto;
import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.notFoundException.MemberNotFoundException;
import com.chatboard.etude.init.TestInitDB;
import com.chatboard.etude.repository.category.CategoryRepository;
import com.chatboard.etude.repository.comment.CommentRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import com.chatboard.etude.service.comment.CommentService;
import com.chatboard.etude.service.sign.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.chatboard.etude.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static com.chatboard.etude.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static com.chatboard.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.chatboard.etude.factory.entity.CommentFactory.createComment;
import static com.chatboard.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class CommentControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB testInitDB;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    SignService signService;

    ObjectMapper objectMapper = new ObjectMapper();

    Member member1, member2, admin;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        testInitDB.initDB();

        member1 = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        member2 = memberRepository.findByEmail(testInitDB.getMember2Email())
                .orElseThrow(MemberNotFoundException::new);
        admin = memberRepository.findByEmail(testInitDB.getAdminEmail())
                .orElseThrow(MemberNotFoundException::new);
        category = categoryRepository.findAll().get(0);
        post = postRepository.save(createPost(member1, category));
    }

    @Test
    void readAllTest() throws Exception {
        // given, when, then
        mockMvc.perform(
                get("/api/comments")
                        .param("postId", String.valueOf(1)))
                .andExpect(status().isOk());
    }

    @Test
    void createTest() throws Exception {
        // given
        CommentCreateRequestDto request = createCommentCreateRequest(
                "content", post.getId(), null, null);
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                testInitDB.getMember1Email(), testInitDB.getPassword()
        ));

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .header("Authorization", signInResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<CommentDto> result = commentService.readAllComments(createCommentReadCondition(post.getId()));
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        CommentCreateRequestDto request = createCommentCreateRequest("content", post.getId(), member1.getId(), null);

        // when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/comments/{id}", comment.getId())
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                testInitDB.getAdminEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Comment comment = commentRepository.save(createComment(member1, post, null));
        SignInResponseDto invalidSignInResponseDto = signService.signIn(createSignInRequest(
                testInitDB.getMember2Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                    delete("/api/comments/{id}", comment.getId())
                    .header("Authorization", invalidSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }
}
