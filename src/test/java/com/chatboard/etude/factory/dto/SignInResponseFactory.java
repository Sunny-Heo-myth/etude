package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.sign.SignInResponse;

public class SignInResponseFactory {

    public static SignInResponse createSignInResponse(String accessToken, String refreshToken) {
        return new SignInResponse(accessToken, refreshToken);
    }
}
