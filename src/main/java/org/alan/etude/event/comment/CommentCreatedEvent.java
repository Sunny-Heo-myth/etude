package org.alan.etude.event.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.alan.etude.dto.member.MemberDto;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {

    private MemberDto publisher;
    private MemberDto postWriter;
    private MemberDto parentWriter;
    private String content;
}
