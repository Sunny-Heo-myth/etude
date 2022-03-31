package com.chatboard.etude.service.sign;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.dto.sign.RefreshTokenResponse;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignInResponse;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.*;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    // auxiliary

    private void validateSignUpInfo(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberEmailAlreadyExistsException(request.getEmail());
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(request.getNickname());
        }
    }

    private void validatePassword(SignInRequest request, Member member) {
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }

    private void validateRefreshToken(String refreshToken) {
        if (!refreshTokenHelper.validate(refreshToken)) {
            throw new AuthenticationEntryPointException();
        }
    }

    // core

    @Transactional
    public void signUp(SignUpRequest request) {
        validateSignUpInfo(request);

        memberRepository.save(SignUpRequest.toEntity(
                request,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                        .orElseThrow(RoleNotFoundException::new),
                passwordEncoder)
        );
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailureException::new);

        validatePassword(request, member);

        String subject = createSubject(member);
        String accessToken = accessTokenHelper.createToken(subject);
        String refreshToken = refreshTokenHelper.createToken(subject);
        return new SignInResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        String subject = refreshTokenHelper.extractSubject(refreshToken);
        String accessToken = accessTokenHelper.createToken(subject);
        return new RefreshTokenResponse(accessToken);
    }

}
