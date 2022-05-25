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

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView signUpPage() {
        return new ModelAndView("/sign/signUpPage");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView signUp(@Valid @RequestBody SignUpRequestDto request) {
        signService.signUp(request);
        return new ModelAndView("/common/index");
    }

    @GetMapping("/sign")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView signInPage() {
        return new ModelAndView("/sign/signInPage");
    }

    @PostMapping("/sign")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView signIn(@RequestBody @Valid SignInRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("/common/index");
        modelAndView.addObject(signService.signIn(request));
        return modelAndView;
    }

}
