package com.chatboard.etude.controller.post;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.post.PostCreateRequestDto;
import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import com.chatboard.etude.service.post.PostService;
import com.chatboard.etude.dto.post.PostPageDto;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readPost(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId) {
        ModelAndView modelAndView = new ModelAndView("/post/postReadPage");
        modelAndView.addObject("post", postService.readPost(postId));
        return modelAndView;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAllPost(@Valid PostReadConditionDto condition) {
        ModelAndView modelAndView = new ModelAndView("/post/postListPage");
        PostPageDto postPageDto = postService.readAllPostWithPage(condition);
        modelAndView.addObject("pageMakerDto", postPageDto);
        modelAndView.addObject("postSimpleDtos", postPageDto.getResult().getContent());
        return modelAndView;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public ModelAndView createPost(@Valid @RequestBody PostCreateRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("/post/postCreatePage");
        modelAndView.addObject(postService.createPost(request));
        return modelAndView;
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView updatePost(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto request) {
        ModelAndView modelAndView = new ModelAndView("/post/postUpdatePage");
        modelAndView.addObject(postService.updatePost(postId, request));
        return modelAndView;
    }
}
