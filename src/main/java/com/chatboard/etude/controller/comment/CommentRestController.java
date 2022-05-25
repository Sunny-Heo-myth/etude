package com.chatboard.etude.controller.comment;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.comment.CommentCreateRequestDto;
import com.chatboard.etude.dto.comment.CommentReadConditionDto;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(
            value = "create comment",
            notes = "Create comment."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody CommentCreateRequestDto request) {
        commentService.createComment(request);
        return Response.success();
    }

    @ApiOperation(
            value = "comment list lookup",
            notes = "Lookup comment list."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAllHierarchical(@Valid CommentReadConditionDto condition) {
        return Response.success(commentService.readAllCommentsHierarchical(condition));
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@PathVariable Long postId) {
        return Response.success(commentService.readAllComments(new CommentReadConditionDto(postId)));
    }

    @ApiOperation(
            value = "update comment",
            notes = "Update comment."
    )
    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @ApiParam(value = "comment id", required = true) @PathVariable Long commentId) {
        commentService.updateComment(commentId);
        return Response.success();
    }


    @ApiOperation(value = "delete comment", notes = "Delete comment.")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @ApiParam(value = "comment id", required = true) @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return Response.success();
    }
}
