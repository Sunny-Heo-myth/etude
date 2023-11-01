package org.alan.etude.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.message.MessageCreateRequestDto;
import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.entity.member.Member;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.init.TestInitDB;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.message.MessageRepository;
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

import static org.alan.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MessageRestControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB testInitDB;
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MessageRepository messageRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Member admin, sender, receiver;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testInitDB.initDB();

        admin = memberRepository.findByEmail(testInitDB.getAdminEmail())
                .orElseThrow(MemberNotFoundException::new);
        sender = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        receiver = memberRepository.findByEmail(testInitDB.getMember2Email())
                .orElseThrow(MemberNotFoundException::new);
    }

    @Test
    void readAllBySenderTest() throws Exception {
        // given
        Integer size = 2;
        SignInResponseDto signInResponseDto = signService.signIn(
                createSignInRequest(sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("size", String.valueOf(size))
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseResult.data.numberOfElements").value(2));
    }

    @Test
    void readAllBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readAllByReceiverTest() throws Exception {
        // given
        Integer size = 2;
        SignInResponseDto signInResponseDto = signService.signIn(
                createSignInRequest(receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver")
                                .param("size", String.valueOf(size))
                                .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseResult.data.numberOfElements").value(2));
    }

    @Test
    void readAllByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/receiver")
                        .param("size", String.valueOf(size)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id)
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto adminSignInResponseDto = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id)
                        .header("Authorization", adminSignInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto notOwnerSignInResponseDto = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id)
                        .header("Authorization", notOwnerSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequestDto request =
                createMessageCreateRequest("content", null, receiver.getId());

        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isCreated());
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        MessageCreateRequestDto request =
                createMessageCreateRequest("content", null, receiver.getId());

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBySenderByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto adminSignInResponseDto = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", adminSignInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBySenderAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto notOwnerSignInResponseDto = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", notOwnerSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    // copy
    @Test
    void deleteByReceiverByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto signInResponseDto = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", signInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto adminSignInResponseDto = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", adminSignInResponseDto.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteByReceiverAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponseDto notOwnerSignInResponseDto = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", notOwnerSignInResponseDto.getAccessToken()))
                .andExpect(status().isForbidden());
    }
}
