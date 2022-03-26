package com.chatboard.etude.entity.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable // for id class
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberRoleId implements Serializable {

    private Member member;
    private Role role;
}
