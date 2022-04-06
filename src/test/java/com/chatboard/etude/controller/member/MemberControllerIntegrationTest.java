package com.chatboard.etude.controller.member;

import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.init.TestInitDB;
import com.chatboard.etude.dto.sign.SignInResponse;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import com.chatboard.etude.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.chatboard.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MemberControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestInitDB testInitDB;
    @Autowired
    private SignService signService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        testInitDB.initDB();
    }

    // check for enum cast
    @Test
    void testInitDBTest() {
        assertThat(roleRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void EnumTest() {
        boolean x = RoleType.valueOf("ROLE_NORMAL") == RoleType.ROLE_NORMAL;
        assertTrue(x);
    }

    // main test
    @Test
    void readTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                get("/api/members/{id}", member.getId()))   // get : permit all
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        SignInResponse response = signService.signIn(
                createSignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", response.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        SignInResponse adminResponse = signService.signIn(
                createSignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", adminResponse.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        SignInResponse attackerResponse = signService.signIn(
                createSignInRequest(testInitDB.getMember2Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", attackerResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void deleteUnauthorizedByRefreshTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        SignInResponse response = signService.signIn(
                createSignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", response.getRefreshToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }
}
