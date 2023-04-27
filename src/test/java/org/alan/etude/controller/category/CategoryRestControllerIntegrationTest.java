package org.alan.etude.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.category.CategoryCreateRequestDto;
import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.init.TestInitDB;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
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

import static org.alan.etude.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class CategoryRestControllerIntegrationTest {

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
    SignService signService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testInitDB.initDB();
    }

    @Test
    void readAllTest() throws Exception {
        // given, when, then
        mockMvc.perform(
                get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void createTest() throws Exception {
        // given
        CategoryCreateRequestDto request = createCategoryCreateRequest();
        SignInResponseDto adminSignInResponseDto = signService.signIn(
                createSignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));

        int beforeSize = categoryRepository.findAll().size();

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .header("Authorization", adminSignInResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<Category> result = categoryRepository.findAll();
        assertThat(result.size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        CategoryCreateRequestDto request = createCategoryCreateRequest();

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        // missing header ("Authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createAccessDeniedByNormalMemberTest() throws Exception {
        // given
        CategoryCreateRequestDto request = createCategoryCreateRequest();
        SignInResponseDto normalMemberSignInResponseDto = signService.signIn(
                createSignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .header("Authorization", normalMemberSignInResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Long id = categoryRepository.findAll().get(0).getId();
        SignInResponseDto adminSignInResponseDto = signService.signIn(
                createSignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", id)
                .header("Authorization", adminSignInResponseDto.getAccessToken()))
                .andExpect(status().isOk());

        List<Category> result = categoryRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void deleteUnauthorizedByNoneToken() throws Exception {
        // given
        Long id = categoryRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAccessDeniedByNormalMemberTest() throws Exception {
        // given
        Long id = categoryRepository.findAll().get(0).getId();
        SignInResponseDto normalSignInResponseDto = signService.signIn(
                createSignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", id)
                .header("Authorization", normalSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }
}
