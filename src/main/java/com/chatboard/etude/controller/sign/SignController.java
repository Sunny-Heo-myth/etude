package com.chatboard.etude.controller.sign;

import com.chatboard.etude.dto.sign.SignInRequestDto;
import com.chatboard.etude.dto.sign.SignUpRequestDto;
import com.chatboard.etude.service.sign.SignService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView signUp(@Valid @RequestBody SignUpRequestDto request) {
        signService.signUp(request);
        return new ModelAndView("/sign/signUpPage");
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView signIn(@Valid @RequestBody SignInRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("/sign/signInPage");
        modelAndView.addObject(signService.signIn(request));
        return modelAndView;
    }

}
