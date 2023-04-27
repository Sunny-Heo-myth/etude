package org.alan.etude.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alan.etude.advice.ExceptionAdvice;
import org.alan.etude.dto.sign.SignInRequestDto;
import org.alan.etude.dto.sign.SignUpRequestDto;
import org.alan.etude.exception.LoginFailureException;
import org.alan.etude.exception.RefreshTokenFailureException;
import org.alan.etude.exception.notFoundException.RoleNotFoundException;
import org.alan.etude.exception.validationException.MemberEmailAlreadyExistsException;
import org.alan.etude.exception.validationException.MemberNicknameAlreadyExistsException;
import org.alan.etude.handler.FailureResponseHandler;
import org.alan.etude.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.alan.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.alan.etude.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignRestControllerAdviceTest {

    @InjectMocks
    SignRestController signRestController;
    @Mock
    SignService signService;
    @Mock
    FailureResponseHandler failureResponseHandler;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("i18n/exception");

        mockMvc = MockMvcBuilders.standaloneSetup(signRestController)
                .setControllerAdvice(new ExceptionAdvice(failureResponseHandler))
                .build();
    }

    @Test
    void signInLoginFailureExceptionTest() throws Exception {
        // given
        SignInRequestDto request = createSignInRequest("email@email.com", "123456a!");
        given(signService.signIn(any()))
                .willThrow(LoginFailureException.class);

        // when, then
        mockMvc.perform(
                post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void signInMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignInRequestDto request = createSignInRequest("email", "1234567");
        
        // when, then
        mockMvc.perform(
                post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpMemberEmailAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequestDto request = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(MemberEmailAlreadyExistsException.class)
                .when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void signUpMemberNicknameAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequestDto request = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(MemberNicknameAlreadyExistsException.class)
                .when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void signUpRoleNotFoundExceptionTest() throws Exception{
        // given
        SignUpRequestDto request = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(RoleNotFoundException.class)
                .when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignUpRequestDto request = createSignUpRequest("", "", "", "");

        // when, then
        mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokenAuthenticationEntryPointException() throws Exception {
        // given
        given(signService.refreshToken(anyString())).willThrow(RefreshTokenFailureException.class);

        // when, then
        mockMvc.perform(
                post("/api/refresh-token")
                        .header("Authorization", "refreshToken"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokenMissingRequestHeaderException() throws Exception {
        // given, when, then
        mockMvc.perform(
                post("/api/refresh-token"))
                .andExpect(status().isBadRequest());
    }

}
