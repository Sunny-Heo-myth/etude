package org.alan.etude.controller.sign;

import jakarta.validation.Valid;
import org.alan.etude.dto.sign.SignInRequestDto;
import org.alan.etude.dto.sign.SignUpRequestDto;
import org.alan.etude.service.sign.SignService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


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
