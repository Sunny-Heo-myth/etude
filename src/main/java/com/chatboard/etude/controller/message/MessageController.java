package com.chatboard.etude.controller.message;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.message.MessageReadCondition;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.message.MessageService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public ModelAndView readAllMessageBySender(@Valid MessageReadCondition condition) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAllMessageBySender(condition));
        return modelAndView;
    }

    @GetMapping("/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public ModelAndView readAllMessageByReceiver(@Valid MessageReadCondition condition) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAllMessageByReceiver(condition));
        return modelAndView;
    }

    @GetMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAMessage(
            @ApiParam(value = "message id", required = true) @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAMessage(id));
        return modelAndView;
    }

}
