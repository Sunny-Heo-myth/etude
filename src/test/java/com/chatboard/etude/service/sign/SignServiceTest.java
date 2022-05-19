package com.chatboard.etude.service.sign;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.dto.sign.RefreshTokenResponseDto;
import com.chatboard.etude.dto.sign.SignInResponseDto;
import com.chatboard.etude.dto.sign.SignUpRequestDto;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.*;
import com.chatboard.etude.exception.notFoundException.RoleNotFoundException;
import com.chatboard.etude.exception.validationException.MemberEmailAlreadyExistsException;
import com.chatboard.etude.exception.validationException.MemberNicknameAlreadyExistsException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

// why import static?
import java.util.List;
import java.util.Optional;

import static com.chatboard.etude.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.chatboard.etude.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    SignService signService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenHelper accessTokenHelper;
    @Mock
    TokenHelper refreshTokenHelper;

    // Mockito can not recognize same type of @Mock.
    @BeforeEach
    void beforeEach() {
        signService = new SignService(
                memberRepository,
                roleRepository,
                passwordEncoder,
                accessTokenHelper,
                refreshTokenHelper
        );
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequestDto request = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL))
                .willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        signService.signUp(request);

        // then
        // (verify whether Password encoder did encode & memberRepository did save)
        verify(passwordEncoder).encode(request.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validateSignUpByDuplicateEmailTest() {
        //given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void validateSignUpByDuplicateNicknameTest() {
        // given
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @Test
    void signUpRoleNotFoundTest() {
        // given
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void signInTest() {
        // given
        given(memberRepository.findWithRolesByEmail(any()))
                .willReturn(Optional.of(createMember()));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);

        given(accessTokenHelper.createToken(any()))
                .willReturn("access");

        given(refreshTokenHelper.createToken(any()))
                .willReturn("refresh");

        // when
        SignInResponseDto response = signService.signIn(createSignInRequest("email", "password"));

        // then
        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findWithRolesByEmail(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(createSignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findWithRolesByEmail(any()))
                .willReturn(Optional.of(createMember()));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(createSignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void refreshTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(refreshTokenHelper.parse(refreshToken))
                .willReturn(Optional.of(new TokenHelper.PrivateClaims("memberId", List.of("ROLE_NORMAL"))));
        given(accessTokenHelper.createToken(any())).willReturn(accessToken);

        // when
        RefreshTokenResponseDto response = signService.refreshToken(refreshToken);

        // then
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void refreshTokenExceptionMyInvalidTokenTest() {
        // given
        String refreshToken = "refreshToken";
        given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(RefreshTokenFailureException.class);
    }

    private Member createMember() {
        return new Member("email", "password", "username", "nickname", emptyList());
    }
}
