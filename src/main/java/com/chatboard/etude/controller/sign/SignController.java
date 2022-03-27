package com.chatboard.etude.controller.sign;

import com.chatboard.etude.controller.response.Response;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest request) {
        signService.signUp(request);
        return Response.success();
    }

    @PostMapping("api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest request) {
        return Response.success(signService.signIn(request));
    }

}
