package org.alan.etude.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.dto.sign.SignInRequestDto;
import org.alan.etude.dto.sign.SignUpRequestDto;
import org.alan.etude.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.alan.etude.factory.dto.RefreshTokenResponseFactory.createRefreshTokenResponse;
import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.alan.etude.factory.dto.SignInResponseFactory.createSignInResponse;
import static org.alan.etude.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignRestControllerTest {

    @InjectMocks
    SignRestController signRestController;

    @Mock
    SignService signService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signRestController).build();
    }

    @Test
    void signUpTest() throws Exception {
        // given
        SignUpRequestDto request = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(signService).signUp(request);
    }

    @Test
    void signInTest() throws Exception {
        // given
        SignInRequestDto request = createSignInRequest("email@email.com", "123456a!");
        given(signService.signIn(request))
                .willReturn(createSignInResponse("access", "refresh"));

        // when, then
        mockMvc.perform(
                post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseResult.data.accessToken").value("access"))
                .andExpect(jsonPath("$.responseResult.data.refreshToken").value("refresh"));

        verify(signService).signIn(request);
    }

    @Test
    void ignoreNullValueInJsonResponseTest() throws Exception {
        // given
        SignUpRequestDto request = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    void refreshTokenTest() throws Exception {
        // given
        given(signService.refreshToken("refreshToken")).willReturn(createRefreshTokenResponse("accessToken"));

        // when, then
        mockMvc.perform(
                post("/api/refresh-token")
                        .header("Authorization", "refreshToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseResult.data.accessToken").value("accessToken"));
    }
}
