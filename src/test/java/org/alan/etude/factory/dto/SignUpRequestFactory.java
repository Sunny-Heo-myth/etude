package org.alan.etude.factory.dto;

import org.alan.etude.dto.sign.SignUpRequestDto;

public class SignUpRequestFactory {

    public static SignUpRequestDto createSignUpRequest() {
        return new SignUpRequestDto("email@email.com", "123456a!", "username", "nickname");
    }

    public static SignUpRequestDto createSignUpRequest(String email, String password, String username, String nickname) {
        return new SignUpRequestDto(email, password, username, nickname);
    }

    public static SignUpRequestDto createSignUpRequestWithEmail(String email) {
        return new SignUpRequestDto(email, "123456a!", "username", "nickname");
    }

    public static SignUpRequestDto createSignUpRequestWithPassword(String password) {
        return new SignUpRequestDto("email@email.com", password, "username", "nickname");
    }

    public static SignUpRequestDto createSignUpRequestWithUsername(String username) {
        return new SignUpRequestDto("email@email.com", "123456a!", username, "nickname");
    }

    public static SignUpRequestDto createSignUpRequestWithNickname(String nickname) {
        return new SignUpRequestDto("email@email.com", "123456a!", "username", nickname);
    }

}
