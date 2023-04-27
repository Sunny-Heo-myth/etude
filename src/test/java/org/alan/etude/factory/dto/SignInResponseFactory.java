package org.alan.etude.factory.dto;

import org.alan.etude.dto.sign.SignInResponseDto;

public class SignInResponseFactory {

    public static SignInResponseDto createSignInResponse(String accessToken, String refreshToken) {
        return new SignInResponseDto(accessToken, refreshToken);
    }
}
