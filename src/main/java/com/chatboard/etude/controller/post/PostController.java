package com.chatboard.etude.controller.post;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.post.PostCreateRequestDto;
import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import com.chatboard.etude.service.post.PostService;
import com.chatboard.etude.dto.post.page.PageMakerDto;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readPost(
            @ApiParam(value = "post id", required = true) @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/post/postExample");
        modelAndView.addObject("post", postService.readPost(id));
        return modelAndView;
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAllPost(@Valid PostReadConditionDto condition) {
        ModelAndView modelAndView = new ModelAndView("/boards/list");
        PageMakerDto pageMakerDto = postService.readAllPostWithPage(condition);
        modelAndView.addObject("postList", pageMakerDto);
        modelAndView.addObject("postSimpleDtos", pageMakerDto.getResult().getContent());
        return modelAndView;
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public ModelAndView createPost(@Valid @ModelAttribute PostCreateRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(postService.createPost(request));
        return modelAndView;
    }

    @PutMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView updatePost(
            @ApiParam(value = "post id", required = true) @PathVariable Long id,
            @Valid @ModelAttribute PostUpdateRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject(postService.updatePost(id, request));
        return modelAndView;
    }
}
