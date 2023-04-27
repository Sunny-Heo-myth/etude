package org.alan.etude.controller.message;

import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.alan.etude.aop.AssignMemberId;
import org.alan.etude.dto.message.MessageReadConditionDto;
import org.alan.etude.service.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/sender")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public ModelAndView readAllMessageBySender(@Valid MessageReadConditionDto condition) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAllMessageBySender(condition));
        return modelAndView;
    }

    @GetMapping("/receiver")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public ModelAndView readAllMessageByReceiver(@Valid MessageReadConditionDto condition) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAllMessageByReceiver(condition));
        return modelAndView;
    }

    @GetMapping("/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAMessage(
            @ApiParam(value = "message id", required = true) @PathVariable Long messageId) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(messageService.readAMessage(messageId));
        return modelAndView;
    }

}
