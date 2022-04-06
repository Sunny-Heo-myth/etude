package com.chatboard.etude.dto.alarm;

import com.chatboard.etude.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmInfoDto {
    private MemberDto targetMemberDto;
    private String message;

}
