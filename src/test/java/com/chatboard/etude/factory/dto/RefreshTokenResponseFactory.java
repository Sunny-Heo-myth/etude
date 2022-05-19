package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.sign.RefreshTokenResponseDto;

public class RefreshTokenResponseFactory {

    public static RefreshTokenResponseDto createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponseDto(accessToken);
    }
}
