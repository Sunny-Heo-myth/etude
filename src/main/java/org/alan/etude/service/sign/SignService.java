package org.alan.etude.service.sign;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.alan.etude.config.token.TokenHelper;
import org.alan.etude.dto.sign.RefreshTokenResponseDto;
import org.alan.etude.dto.sign.SignInRequestDto;
import org.alan.etude.dto.sign.SignInResponseDto;
import org.alan.etude.dto.sign.SignUpRequestDto;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.member.MemberRole;
import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;
import org.alan.etude.exception.LoginFailureException;
import org.alan.etude.exception.RefreshTokenFailureException;
import org.alan.etude.exception.notFoundException.RoleNotFoundException;
import org.alan.etude.exception.validationException.MemberEmailAlreadyExistsException;
import org.alan.etude.exception.validationException.MemberNicknameAlreadyExistsException;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.role.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Sign controller", tags = "Sign")
@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDto request) {

        validateSignUpInfo(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Person who wants to do normal sign up must sign up with RoleType.ROLE_NORMAL.
        // and RoleTypes must be saved as in db beforehand.
        List<Role> roles = List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                .orElseThrow(RoleNotFoundException::new));

        memberRepository.save(
                new Member(request.getEmail(), encodedPassword, request.getUsername(), request.getNickname(), roles)
        );
    }

    public SignInResponseDto signIn(SignInRequestDto request) {

        // find in DB FIRST before authenticate :
        // after member deletion,
        // even if token is valid, member can not be found.
        Member member = memberRepository.findWithRolesByEmail(request.getEmail())
                .orElseThrow(LoginFailureException::new);

        validatePassword(request, member);

        // privateClaims fields are
        // 1. id
        // 2. roleType Strings
        // which goes into JWT itself.
        TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);

        String accessToken = accessTokenHelper.createToken(privateClaims);
        String refreshToken = refreshTokenHelper.createToken(privateClaims);

        return new SignInResponseDto(accessToken, refreshToken);
    }

    public RefreshTokenResponseDto refreshToken(String refreshToken) {

        TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(refreshToken)
                .orElseThrow(RefreshTokenFailureException::new);

        String accessToken = accessTokenHelper.createToken(privateClaims);
        return new RefreshTokenResponseDto(accessToken);
    }



    // checking uniqueness of email and nickname when sign-up
    private void validateSignUpInfo(SignUpRequestDto request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberEmailAlreadyExistsException(request.getEmail());
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(request.getNickname());
        }
    }

    // check request raw password and encoded db password when sign-in
    private void validatePassword(SignInRequestDto request, Member member) {
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
