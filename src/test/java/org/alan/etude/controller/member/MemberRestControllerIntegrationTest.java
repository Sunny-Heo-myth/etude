package org.alan.etude.controller.member;

import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.member.RoleType;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.init.TestInitDB;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.role.RoleRepository;
import org.alan.etude.service.sign.SignService;
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

import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MemberRestControllerIntegrationTest {

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
        SignInResponseDto response = signService.signIn(
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
        SignInResponseDto adminResponse = signService.signIn(
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);

        SignInResponseDto attackerResponse = signService.signIn(
                createSignInRequest(testInitDB.getMember2Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", attackerResponse.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnauthorizedByRefreshTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getMember1Email())
                .orElseThrow(MemberNotFoundException::new);
        SignInResponseDto response = signService.signIn(
                createSignInRequest(testInitDB.getMember1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", response.getRefreshToken()))
                .andExpect(status().isUnauthorized());
    }
}
