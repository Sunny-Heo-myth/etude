package com.chatboard.etude.factory.dto;

import com.chatboard.etude.dto.sign.SignInRequestDto;

public class SignInRequestFactory {

    public static SignInRequestDto createSignInRequest() {
        return new SignInRequestDto("email@email.com", "123456@!");
    }

    public static SignInRequestDto createSignInRequest(String email, String password) {
        return new SignInRequestDto(email, password);
    }

    public static SignInRequestDto createSignInRequestWithEmail(String email) {
        return new SignInRequestDto(email, "123456a!");
    }

    public static SignInRequestDto createSignInRequestWithPassword(String password) {
        return new SignInRequestDto("email@email.com", password);
    }

}
