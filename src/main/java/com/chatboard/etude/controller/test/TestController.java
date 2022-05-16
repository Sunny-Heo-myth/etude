package com.chatboard.etude.controller.test;

import com.chatboard.etude.dto.test.TestMemberVO;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestMapping("/test")
@Controller
@Slf4j
public class TestController {

    MessageSource messageSource;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView test() {
        TestMemberVO vo = new TestMemberVO(123, "a", "a", "a", new Timestamp(System.currentTimeMillis()));
        ModelAndView modelAndView = new ModelAndView("/layout/sample1");
        modelAndView.addObject("vo", vo);
        return modelAndView;
    }

    @GetMapping("/test2")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView test2() {
        ModelAndView modelAndView = new ModelAndView("/layout/layout1");
        modelAndView.addObject("greeting", "hello world2");
        return modelAndView;
    }

    @GetMapping("/test4")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView test4() {
        ModelAndView modelAndView = new ModelAndView("/layout/sample4");
        modelAndView.addObject("string", "hello string");
        return modelAndView;
    }

    @GetMapping("/eachTest")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView eachTest() {
        ModelAndView modelAndView = new ModelAndView("/layout/sample3");
        List<TestMemberVO> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestMemberVO(
                    i,
                    "user" + i,
                    "password" + i,
                    "hong" + i,
                    new Timestamp(System.currentTimeMillis()))
            );
        }
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    @GetMapping("/sample/hello")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView hello() {
        return new ModelAndView("/sample/hello");
    }

    @GetMapping("/format")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView formatTest() {
        ModelAndView modelAndView = new ModelAndView("/test/test");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleType.ROLE_ADMIN));
        roles.add(new Role(RoleType.ROLE_NORMAL));
        modelAndView.addObject("member",
                new Member("email", "password", "username", "nickname", roles));
        return modelAndView;
    }

    @PostMapping("/sample/postSample")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView postTest(
            @RequestParam(value = "sthInUrl", defaultValue = "1") Integer pageNum) {
            return new ModelAndView();
    }

    @GetMapping("/localeTest")
    @ResponseStatus(HttpStatus.OK)
    public String locale(@RequestHeader(value = "Accept-Language", required = false)Locale locale) {
        log.info(locale.toString());
        return messageSource.getMessage("test.message", null, locale);
    }

}
