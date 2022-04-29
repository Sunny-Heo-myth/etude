package com.chatboard.etude.controller.sign;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.dto.sign.SignInRequest;
import com.chatboard.etude.dto.sign.SignUpRequest;
import com.chatboard.etude.service.sign.SignService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.chatboard.etude.dto.response.Response.success;

@RequiredArgsConstructor
@Controller
public class SignController {

    private final SignService signService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView signUp(@Valid @RequestBody SignUpRequest request) {
        signService.signUp(request);
        return new ModelAndView("view");
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView signIn(@Valid @RequestBody SignInRequest request) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(signService.signIn(request));
        return modelAndView;
    }

//    @PostMapping("/refresh-token")
//    @ResponseStatus(HttpStatus.OK)
//    public ModelAndView refreshToken(
//            @ApiIgnore @RequestHeader(value = "Authorization") String refreshToken) {
//        ModelAndView modelAndView = new ModelAndView("view");
//        modelAndView.addObject()
//        return success(signService.refreshToken(refreshToken));
//    }
}
