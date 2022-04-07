package com.chatboard.etude.service.sign;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.dto.sign.RefreshTokenResponse;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignInResponse;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.MemberRole;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.*;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Api(value = "Sign controller", tags = "Sign")
@Service
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

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
        Member member = memberRepository.findWithRolesByEmail(request.getEmail())
                .orElseThrow(LoginFailureException::new);

        validatePassword(request, member);

        TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        String refreshToken = refreshTokenHelper.createToken(privateClaims);
        return new SignInResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(refreshToken)
                .orElseThrow(RefreshTokenFailureException::new);

        String accessToken = accessTokenHelper.createToken(privateClaims);
        return new RefreshTokenResponse(accessToken);
    }

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

    private TokenHelper.PrivateClaims createPrivateClaims(Member member) {
        return new TokenHelper.PrivateClaims(
                String.valueOf(member.getId()),
                member.getRoles().stream()
                        .map(MemberRole::getRole)
                        .map(Role::getRoleType)
                        .map(RoleType::toString)
                        .collect(Collectors.toList()));
    }

}
