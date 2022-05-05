package com.chatboard.etude.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FrontController {

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/authenticate/signIn");
        return modelAndView;
    }

    @GetMapping("/index2")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView index2() {
        return new ModelAndView("/common/index");
    }
}
