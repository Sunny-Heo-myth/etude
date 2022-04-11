package com.chatboard.etude.entity.member;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable // for id class
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberRoleId implements Serializable {

    // order of creating composite key is alphabetical order
    @ManyToOne
    @JoinColumn(name = "member_member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "role_role_id")
    private Role role;
}
