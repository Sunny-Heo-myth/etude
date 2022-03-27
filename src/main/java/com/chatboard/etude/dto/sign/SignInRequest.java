package com.chatboard.etude.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Email(message = "Please enter the email format.")
    @NotBlank(message = "Please enter your email.")
    private String email;

    @NotBlank(message = "Please enter your password.")
    private String password;
}
