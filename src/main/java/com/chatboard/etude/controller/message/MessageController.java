package com.chatboard.etude.controller.message;

import com.chatboard.etude.aop.AssignMemberId;
import com.chatboard.etude.dto.message.MessageCreateRequest;
import com.chatboard.etude.dto.message.MessageReadCondition;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Message Controller", tags = "Message")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "Read messages from the sender", notes = "Read messages from the sender.")
    @GetMapping("/api/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllBySender(@Valid MessageReadCondition condition) {
        return Response.success(messageService.readAllBySender(condition));
    }

    @ApiOperation(value = "Read messages for the receiver", notes = "Read messages for the receiver.")
    @GetMapping("/api/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllByReceiver(@Valid MessageReadCondition condition) {
        return Response.success(messageService.readAllByReceiver(condition));
    }

    @ApiOperation(value = "read messages", notes = "Read messages.")
    @GetMapping("/api/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "message id", required = true) @PathVariable Long id) {
        return Response.success(messageService.read(id));
    }

    @ApiOperation(value = "create message", notes = "Create message.")
    @PostMapping("/api/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody MessageCreateRequest request) {
        messageService.create(request);
        return Response.success();
    }

    @ApiOperation(value = "delete messages by sender", notes = "Delete messages by sender.")
    @DeleteMapping("/api/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBySender(@ApiParam(value = "message id", required = true) @PathVariable Long id) {
        messageService.deleteBySender(id);
        return Response.success();
    }

    @ApiOperation(value = "delete messages by receiver", notes = "Delete messages by receiver.")
    @DeleteMapping("/api/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteByReceiver(@ApiParam(value = "message id", required = true) @PathVariable Long id) {
        messageService.deleteByReceiver(id);
        return Response.success();
    }
}
