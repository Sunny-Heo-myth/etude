package com.chatboard.etude.dto.member;

import com.chatboard.etude.entity.member.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String username;
    private String nickname;

    // Member to MemberDto
    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getNickname());
    }

    public static MemberDto empty() {
        return new MemberDto(null, "", "" ,"");
    }
}
