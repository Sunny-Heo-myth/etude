package org.alan.etude.entity.member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

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
