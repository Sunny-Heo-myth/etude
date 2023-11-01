package org.alan.etude.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.comment.CommentCreateRequestDto;
import org.alan.etude.dto.comment.CommentDto;
import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.init.TestInitDB;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.comment.CommentRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.alan.etude.service.comment.CommentService;
import org.alan.etude.service.sign.SignService;
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

import static org.alan.etude.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static org.alan.etude.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.alan.etude.factory.entity.CommentFactory.createComment;
import static org.alan.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

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

        List<CommentDto> result = commentService.readAllCommentsHierarchical(createCommentReadCondition(post.getId()));
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
