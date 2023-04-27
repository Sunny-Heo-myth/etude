package org.alan.etude.dto.sign;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {

    @ApiModelProperty(
            value = "email",
            notes = "Please enter your email.",
            required = true,
            example = "member@email.com")
    @Email(message = "{signInRequest.email.email}")
    @NotBlank(message = "{signInRequest.email.notBlank}")
    private String email;

    @ApiModelProperty(
            value = "password",
            notes = "Password must be at least 8 digits and include at least 1 of alphabet, number, special character each.",
            required = true,
            example = "123456a!")
    @NotBlank(message = "{signInRequest.password.notBlank}")
    private String password;
}
