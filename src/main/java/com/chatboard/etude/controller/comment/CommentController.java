package com.chatboard.etude.controller.comment;

import com.chatboard.etude.dto.comment.CommentReadCondition;
import com.chatboard.etude.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAllComments(@Valid CommentReadCondition condition) {
        ModelAndView modelAndView = new ModelAndView("/api/comments");
        modelAndView.addObject(commentService.readAllComments(condition));
        return modelAndView;
    }

}
