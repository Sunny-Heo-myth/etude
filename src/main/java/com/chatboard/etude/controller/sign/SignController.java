package com.chatboard.etude.controller.sign;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.service.sign.SignService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.chatboard.etude.dto.response.Response.success;


@Controller
public class SignController {

    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView signUp(@Valid @RequestBody SignUpRequest request) {
        signService.signUp(request);
        return new ModelAndView("/sign/signUpPage");
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView signIn(@Valid @RequestBody SignInRequest request) {
        ModelAndView modelAndView = new ModelAndView("/sign/signInPage");
        modelAndView.addObject(signService.signIn(request));
        return modelAndView;
    }

}
