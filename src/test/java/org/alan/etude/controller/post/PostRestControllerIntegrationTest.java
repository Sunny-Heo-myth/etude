package org.alan.etude.controller.post;

import org.alan.etude.dto.post.PostCreateRequestDto;
import org.alan.etude.dto.post.PostReadConditionDto;
import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.PostNotFoundException;
import org.alan.etude.init.TestInitDB;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.alan.etude.service.post.PostService;
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

import static org.alan.etude.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static org.alan.etude.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.alan.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class PostRestControllerIntegrationTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestInitDB testInitDB;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    SignService signService;
    @Autowired
    PostService postService;

    Member member1, member2, admin;
    Category category;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testInitDB.initDB();

        member1 = memberRepository.findByEmail(testInitDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        member2 = memberRepository.findByEmail(testInitDB.getMember2Email()).orElseThrow(MemberNotFoundException::new);
        admin = memberRepository.findByEmail(testInitDB.getAdminEmail()).orElseThrow(MemberNotFoundException::new);

        category = categoryRepository.findAll().get(0);
    }

    @Test
    void readAllTest() throws Exception {
        // given
        PostReadConditionDto condition = createPostReadCondition(0, 1);

        // when, then
        mockMvc.perform(
                get("/api/posts")
                        .param("page", String.valueOf(condition.getPage()))
                        .param("size", String.valueOf(condition.getSize()))
                        .param("categoryId",
                                String.valueOf(1),
                                String.valueOf(2))
                        .param("memberId",
                                String.valueOf(1),
                                String.valueOf(2)))
                .andExpect(status().isOk());

    }
    @Test
    void createTest() throws Exception {
        // given
        SignInResponseDto signInResponseDto = signService.signIn(
                createSignInRequest(member1.getEmail(), testInitDB.getPassword()));
        PostCreateRequestDto request = createPostCreateRequest(
                "title", "content", 1000L, member1.getId(), category.getId(), List.of());

        // when, then
        mockMvc.perform(
                multipart("/api/posts")
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("price", String.valueOf(request.getPrice()))
                        .param("categoryId", String.valueOf(request.getCategoryId()))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("POST");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isCreated());
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws  Exception {
        // given
        PostCreateRequestDto request = createPostCreateRequest(
                "title", "content", 1000L, member1.getId(), category.getId(), List.of());

        // when, then
        mockMvc.perform(
                multipart("/api/posts")
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("price", String.valueOf(request.getPrice()))
                        .param("categoryId", String.valueOf(request.getCategoryId()))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("POST");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateByResourceOwnerTest() throws Exception {
        // given
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(member1.getEmail(), testInitDB.getPassword()));
        Post post = postRepository.save(createPost(member1, category));
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L;

        // when, then
        mockMvc.perform(
                multipart("/api/posts/{id}", post.getId())
                        .param("title", updatedTitle)
                        .param("content", updatedContent)
                        .param("price", String.valueOf(updatedPrice))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("PUT");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);

        assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updatedContent);
        assertThat(updatedPost.getPrice()).isEqualTo(updatedPrice);
    }

    @Test
    void updateByAdminTest() throws Exception {
        // given
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(admin.getEmail(), testInitDB.getPassword()));
        Post post = postRepository.save(createPost(member1, category));
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L;

        // when, then
        mockMvc.perform(
                multipart("/api/posts/{id}", post.getId())
                        .param("title", updatedTitle)
                        .param("content", updatedContent)
                        .param("price", String.valueOf(updatedPrice))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("PUT");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);

        assertThat(updatedPost.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updatedContent);
        assertThat(updatedPost.getPrice()).isEqualTo(updatedPrice);
    }

    @Test
    void updateUnauthorizedByNoneTokenTest() throws Exception {
        // given

        Post post = postRepository.save(createPost(member1, category));
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L;

        // when, then
        mockMvc.perform(
                multipart("/api/posts/{id}", post.getId())
                        .param("title", updatedTitle)
                        .param("content", updatedContent)
                        .param("price", String.valueOf(updatedPrice))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("PUT");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))

                .andExpect(status().isUnauthorized());

    }

    @Test
    void updateAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        SignInResponseDto notOwnerSignInResponseDto = signService.signIn(createSignInRequest(member2.getEmail(), testInitDB.getPassword()));
        Post post = postRepository.save(createPost(member1, category));
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        Long updatedPrice = 1234L;

        // when, then
        mockMvc.perform(
                multipart("/api/posts/{id}", post.getId())
                        .param("title", updatedTitle)
                        .param("content", updatedContent)
                        .param("price", String.valueOf(updatedPrice))
                        .with(requestProcessor -> {
                            requestProcessor.setMethod("PUT");
                            return requestProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", notOwnerSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByResourceOwnerTest() throws Exception {
        // given
        Post post = postRepository.save(createPost(member1, category));
        SignInResponseDto signInResponseDto = signService.signIn(
                createSignInRequest(member1.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/posts/{id}", post.getId())
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> postService.readPost(post.getId()))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Post post = postRepository.save(createPost(member1, category));
        SignInResponseDto adminSignInResponseDto = signService.signIn(
                createSignInRequest(admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/posts/{id}", post.getId())
                        .header("Authorization", adminSignInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> postService.readPost(post.getId()))
                .isInstanceOf(PostNotFoundException.class);
    }

}
