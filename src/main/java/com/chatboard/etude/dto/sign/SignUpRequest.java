package com.chatboard.etude.dto.sign;

import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "Sign Up Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @ApiModelProperty(
            value = "email",
            notes = "Please enter your email.",
            required = true,
            example = "member@email.com")
    @Email(message = "{signUpRequest.email.email}")
    @NotBlank(message = "{signUpRequest.email.notBlank}")
    private String email;

    @ApiModelProperty(
            value = "password",
            notes = "Password must be at least 8 digits and include at least 1 of alphabet, number, special character each.",
            required = true,
            example = "123456a!")
    @NotBlank(message = "{signUpRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "{signUpRequest.password.pattern}")
    private String password;

    @ApiModelProperty(
            value = "username",
            notes = "Password must be at least 8 digits and include at least 1 of alphabet, number, special character each.",
            required = true,
            example = "sunny")
    @NotBlank(message = "{signUpRequest.username.notBlank}")
    @Size(min = 2, message = "{signUpRequest.username.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$",
            message = "{signUpRequest.username.pattern}")
    private String username;

    @ApiModelProperty(
            value = "nickname",
            notes = "Please use only Korean and English alphabet as a nickname.",
            required = true,
            example = "hsymyth")
    @NotBlank(message = "{signUpRequest.nickname.notBlank}")
    @Size(min = 2, message = "{signUpRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$",
            message = "{signUpRequest.nickname.pattern}")
    private String nickname;

}
