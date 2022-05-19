package com.chatboard.etude.service.sign;

import com.chatboard.etude.config.token.TokenHelper;
import com.chatboard.etude.dto.sign.RefreshTokenResponseDto;
import com.chatboard.etude.dto.sign.SignInRequestDto;
import com.chatboard.etude.dto.sign.SignInResponseDto;
import com.chatboard.etude.dto.sign.SignUpRequestDto;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.MemberRole;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.*;
import com.chatboard.etude.exception.notFoundException.RoleNotFoundException;
import com.chatboard.etude.exception.validationException.MemberEmailAlreadyExistsException;
import com.chatboard.etude.exception.validationException.MemberNicknameAlreadyExistsException;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import io.swagger.annotations.Api;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Sign controller", tags = "Sign")
@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    public SignService(MemberRepository memberRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       TokenHelper accessTokenHelper,
                       TokenHelper refreshTokenHelper) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenHelper = accessTokenHelper;
        this.refreshTokenHelper = refreshTokenHelper;
    }

    @Transactional
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

    @Transactional(readOnly = true)
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
