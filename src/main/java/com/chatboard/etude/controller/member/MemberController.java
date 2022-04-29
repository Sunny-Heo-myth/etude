package com.chatboard.etude.controller.member;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView read(
            @ApiParam(value = "user id", required = true) @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/member");
        modelAndView.addObject(memberService.readMember(id));
        return modelAndView;
    }

}
