package org.alan.etude.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.alan.etude.dto.member.MemberDto;

@Data
@AllArgsConstructor
public class AlarmInfoDto {
    private MemberDto targetMemberDto;
    private String message;
}
