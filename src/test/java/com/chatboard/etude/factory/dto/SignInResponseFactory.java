package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.sign.SignInResponseDto;

public class SignInResponseFactory {

    public static SignInResponseDto createSignInResponse(String accessToken, String refreshToken) {
        return new SignInResponseDto(accessToken, refreshToken);
    }
}
