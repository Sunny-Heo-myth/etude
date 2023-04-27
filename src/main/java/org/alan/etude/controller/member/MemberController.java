package org.alan.etude.controller.member;

import io.swagger.annotations.ApiParam;
import org.alan.etude.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView read(
            @ApiParam(value = "user id", required = true) @PathVariable Long memberId) {
        ModelAndView modelAndView = new ModelAndView("/member/memberInfoPage");
        modelAndView.addObject(memberService.readMember(memberId));
        return modelAndView;
    }

}
