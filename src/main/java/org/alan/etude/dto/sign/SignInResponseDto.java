package org.alan.etude.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
