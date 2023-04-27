package org.alan.etude.factory.dto;

import org.alan.etude.dto.sign.RefreshTokenResponseDto;

public class RefreshTokenResponseFactory {

    public static RefreshTokenResponseDto createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponseDto(accessToken);
    }
}
