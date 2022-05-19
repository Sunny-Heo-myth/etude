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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(
            value = "read post",
            notes = "Read post."
    )
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId) {
        return Response.success(postService.readPost(postId));
    }

    @ApiOperation(
            value = "post list read",
            notes = "Read post list."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid PostReadConditionDto condition) {
        return Response.success(postService.readAllPost(condition));
    }

    @ApiOperation(
            value = "post create",
            notes = "Create post."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(
            @Valid @RequestBody PostCreateRequestDto request) {
        return Response.success(postService.createPost(request));
    }

    @ApiOperation(
            value = "delete post",
            notes = "Delete post."
    )
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId) {
        postService.deletePost(postId);
        return Response.success();
    }

    @ApiOperation(
            value = "update post",
            notes = "Update post."
    )
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @ApiParam(value = "post id", required = true) @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto request) {
        return Response.success(postService.updatePost(postId, request));
    }

}
