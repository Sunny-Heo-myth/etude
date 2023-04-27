package org.alan.etude.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alan.etude.entity.member.Member;

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

    public static MemberDto emptyMemberDto() {
        return new MemberDto(null, "", "" ,"");
    }
}
