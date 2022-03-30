package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.sign.RefreshTokenResponse;

public class RefreshTokenResponseFactory {

    public static RefreshTokenResponse createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponse(accessToken);
    }
}
