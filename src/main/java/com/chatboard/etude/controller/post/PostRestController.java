package com.chatboard.etude.controller.post;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.post.PostCreateRequestDto;
import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostRestController {

    private final PostService postService;

    @ApiOperation(value = "read post", notes = "Read post.")
    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @ApiParam(value = "post id", required = true) @PathVariable Long id) {
        return Response.success(postService.readPost(id));
    }

    @ApiOperation(value = "post list read", notes = "Read post list.")
    @GetMapping("/api/posts")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid PostReadConditionDto condition) {
        return Response.success(postService.readAllPost(condition));
    }

    @ApiOperation(value = "post create", notes = "Create post.")
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(
            // BindException for validation violation
            // MethodArgumentNotValidException(subset of BindException) for others
            @Valid @ModelAttribute PostCreateRequestDto request) {
        return Response.success(postService.createPost(request));
    }

    @ApiOperation(value = "delete post", notes = "Delete post.")
    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @ApiParam(value = "post id", required = true) @PathVariable Long id) {
        postService.deletePost(id);
        return Response.success();
    }

    @ApiOperation(value = "update post", notes = "Update post.")
    @PutMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @ApiParam(value = "post id", required = true) @PathVariable Long id,
            @Valid @ModelAttribute PostUpdateRequestDto request) {
        return Response.success(postService.updatePost(id, request));
    }

}
