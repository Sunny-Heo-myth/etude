package com.chatboard.etude.controller.member;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "member information read", notes = "Read member information.")
    @GetMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "user id", required = true)
                             @PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

    @ApiOperation(value = "member information deletion", notes = "Delete member information.")
    @DeleteMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "user id", required = true)
                               @PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }
}
