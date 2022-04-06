package com.chatboard.etude.controller.message;

import com.chatboard.etude.dto.message.MessageCreateRequest;
import com.chatboard.etude.dto.sign.SignInResponse;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.init.TestInitDB;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.message.MessageRepository;
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

import static com.chatboard.etude.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static com.chatboard.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MessageControllerIntegrationTest {

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
        SignInResponse signInResponse = signService.signIn(
                createSignInRequest(sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("size", String.valueOf(size))
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.numberOfElements").value(2));
    }

    @Test
    void readAllBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("size", String.valueOf(size)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readAllByReceiverTest() throws Exception {
        // given
        Integer size = 2;
        SignInResponse signInResponse = signService.signIn(
                createSignInRequest(receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver")
                                .param("size", String.valueOf(size))
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.numberOfElements").value(2));
    }

    @Test
    void readAllByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/receiver")
                        .param("size", String.valueOf(size)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id)
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInResponse = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        get("/api/messages/{id}", id)
                                .header("Authorization", adminSignInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void readUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notOwnerSignInResponse = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id)
                        .header("Authorization", notOwnerSignInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequest request =
                createMessageCreateRequest("content", null, receiver.getId());

        SignInResponse signInResponse = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isCreated());
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        MessageCreateRequest request =
                createMessageCreateRequest("content", null, receiver.getId());

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteBySenderByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInResponse = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", adminSignInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBySenderUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteBySenderAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notOwnerSignInResponse = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id)
                        .header("Authorization", notOwnerSignInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    // copy
    @Test
    void deleteByReceiverByResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse signInResponse = signService.signIn(createSignInRequest(
                receiver.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverByAdminTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse adminSignInResponse = signService.signIn(createSignInRequest(
                admin.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", adminSignInResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByReceiverUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteByReceiverAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Long id = messageRepository.findAll().get(0).getId();
        SignInResponse notOwnerSignInResponse = signService.signIn(createSignInRequest(
                sender.getEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id)
                                .header("Authorization", notOwnerSignInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }
}
