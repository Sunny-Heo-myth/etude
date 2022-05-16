package com.chatboard.etude.controller.sign;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.service.sign.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.chatboard.etude.dto.response.Response.success;

@Api(value = "Sign Controller", tags = "Sign")
@RestController
@RequestMapping("/api")
public class SignRestController {

    private final SignService signService;

    public SignRestController(SignService signService) {
        this.signService = signService;
    }

    @ApiOperation(
            value = "sign up",
            notes = "Sign up."
    )
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest request) {
        signService.signUp(request);
        return success();
    }

    @ApiOperation(
            value = "sign in",
            notes = "Sign in."
    )
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest request) {
        return success(signService.signIn(request));
    }

    @ApiOperation(
            value = "refresh token",
            notes = "Refresh access token by refresh token."
    )
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@ApiIgnore @RequestHeader(value = "Authorization") String refreshToken) {
        return success(signService.refreshToken(refreshToken));
    }

}
