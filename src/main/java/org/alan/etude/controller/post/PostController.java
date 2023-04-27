package org.alan.etude.controller.post;

import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.alan.etude.aop.AssignMemberId;
import org.alan.etude.dto.post.PostCreateRequestDto;
import org.alan.etude.dto.post.PostPageDto;
import org.alan.etude.dto.post.PostReadConditionDto;
import org.alan.etude.dto.post.PostUpdateRequestDto;
import org.alan.etude.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public ModelAndView getPostCreatePage() {
        return new ModelAndView("/post/postCreatePage");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public String createPost(
            @Valid @ModelAttribute("request") PostCreateRequestDto request,
            RedirectAttributes redirectAttributes) {

        postService.createPost(request);

        redirectAttributes.addFlashAttribute("msg", "success");

        return "redirect:/list";
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readPost(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @ModelAttribute("readCondition") PostReadConditionDto conditionDto) {

        ModelAndView modelAndView = new ModelAndView("/post/postReadPage");
        modelAndView.addObject("readCondition", conditionDto);
        modelAndView.addObject("postVO", postService.readPost(postId));

        return modelAndView;
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAllPost(@Valid PostReadConditionDto condition) {

        PostPageDto postPageDto = postService.readAllPostWithPage(condition);

        ModelAndView modelAndView = new ModelAndView("/post/postListPage");
        modelAndView.addObject("pageMakerDto", postPageDto);
        modelAndView.addObject("postSimpleDtos", postPageDto.getResult().getContent());

        return modelAndView;
    }

    @GetMapping("/{postId}/update")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView GetPostUpdatePage(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @ModelAttribute("readCondition") PostReadConditionDto conditionDto) {

        ModelAndView modelAndView = new ModelAndView("/post/postUpdatePage");
        modelAndView.addObject("readCondition", conditionDto);
        modelAndView.addObject("postVO", postService.readPost(postId));

        return modelAndView;
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String updatePost(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @ModelAttribute("readCondition") PostReadConditionDto conditionDto,
            @Valid @ModelAttribute PostUpdateRequestDto request,
            RedirectAttributes redirectAttributes) {

        postService.updatePost(postId, request);

        redirectAttributes.addFlashAttribute("msg", "success");

        redirectAttributes.addAttribute("id", postId);
        redirectAttributes.addAttribute("page", conditionDto.getPage());
        redirectAttributes.addAttribute("size", conditionDto.getSize());
        redirectAttributes.addAttribute("categoryId", conditionDto.getCategoryId());
        redirectAttributes.addAttribute("memberId", conditionDto.getMemberId());

        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @ModelAttribute("readCondition") PostReadConditionDto conditionDto,
            RedirectAttributes redirectAttributes) {

        postService.deletePost(postId);

        redirectAttributes.addFlashAttribute("msg", "success");
        redirectAttributes.addAttribute("page", conditionDto.getPage());
        redirectAttributes.addAttribute("size", conditionDto.getSize());
        redirectAttributes.addAttribute("categoryId", conditionDto.getCategoryId());
        redirectAttributes.addAttribute("memberId", conditionDto.getMemberId());

        return "redirect:/list";
    }
}
