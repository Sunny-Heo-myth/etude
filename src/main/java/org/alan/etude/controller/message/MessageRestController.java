package org.alan.etude.controller.message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.alan.etude.aop.AssignMemberId;
import org.alan.etude.dto.message.MessageCreateRequestDto;
import org.alan.etude.dto.message.MessageReadConditionDto;
import org.alan.etude.dto.response.Response;
import org.alan.etude.service.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Message Controller", tags = "Message")
@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation(
            value = "create message",
            notes = "Create message."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody MessageCreateRequestDto request) {
        messageService.createMessage(request);
        return Response.success();
    }

    @ApiOperation(
            value = "Read messages from the sender",
            notes = "Read messages from the sender."
    )
    @GetMapping("/sender")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllBySender(@Valid MessageReadConditionDto condition) {
        return Response.success(messageService.readAllMessageBySender(condition));
    }

    @ApiOperation(
            value = "Read messages for the receiver",
            notes = "Read messages for the receiver."
    )
    @GetMapping("/receiver")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllByReceiver(@Valid MessageReadConditionDto condition) {
        return Response.success(messageService.readAllMessageByReceiver(condition));
    }

    @ApiOperation(
            value = "read messages",
            notes = "Read messages."
    )
    @GetMapping("/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "message id", required = true) @PathVariable Long messageId) {
        return Response.success(messageService.readAMessage(messageId));
    }

    @ApiOperation(
            value = "delete messages by sender",
            notes = "Delete messages by sender."
    )
    @DeleteMapping("/sender/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBySender(
            @ApiParam(value = "message id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageBySender(messageId);
        return Response.success();
    }

    @ApiOperation(
            value = "delete messages by receiver",
            notes = "Delete messages by receiver."
    )
    @DeleteMapping("/receiver/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteByReceiver(
            @ApiParam(value = "message id", required = true) @PathVariable Long messageId) {
        messageService.deleteMessageByReceiver(messageId);
        return Response.success();
    }
}
