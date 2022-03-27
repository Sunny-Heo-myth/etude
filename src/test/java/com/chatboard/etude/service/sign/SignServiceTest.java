package com.chatboard.etude.service.sign;

import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignInResponse;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.LoginFailureException;
import com.chatboard.etude.exception.MemberEmailAlreadyExistsException;
import com.chatboard.etude.exception.MemberNickNameAlreadyExistsException;
import com.chatboard.etude.exception.RoleNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

// why import static?
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    @InjectMocks
    SignService signService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenService tokenService;

    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email", "password", "username", "nickname");
    }

    private Member createMember() {
        return new Member("email", "password", "username", "nickname", emptyList());
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequest request = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL))
                .willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        signService.signUp(request);

        // then (verify whether Password encoder did encode & memberRepository did save)
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

        //wen, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNickNameAlreadyExistsException.class);
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
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(createMember()));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);

        given(tokenService.createAccessToken(anyString()))
                .willReturn("access");

        given(tokenService.createRefreshToken(anyString()))
                .willReturn("refresh");

        // when
        SignInResponse response = signService.signIn(new SignInRequest("email", "password"));

        // then
        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(createMember()));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }
}
