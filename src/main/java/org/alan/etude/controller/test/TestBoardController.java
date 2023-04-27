package org.alan.etude.controller.test;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/boards/")
public class TestBoardController {

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView boardList() {
        ModelAndView modelAndView = new ModelAndView("/boards/list");
        return modelAndView;
    }
}
