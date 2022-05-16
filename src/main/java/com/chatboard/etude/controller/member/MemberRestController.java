package com.chatboard.etude.controller.member;

import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "member controller", tags = "member")
@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(
            value = "member information read",
            notes = "Read member information."
    )
    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @ApiParam(value = "user id", required = true) @PathVariable Long memberId) {
        return Response.success(memberService.readMember(memberId));
    }

    @ApiOperation(
            value = "member information deletion",
            notes = "Delete member information."
    )
    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @ApiParam(value = "user id", required = true) @PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return Response.success();
    }
}
