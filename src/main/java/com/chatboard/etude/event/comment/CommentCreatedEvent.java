package com.chatboard.etude.event.comment;

import com.chatboard.etude.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {

    private MemberDto publisher;
    private MemberDto postWriter;
    private MemberDto parentWriter;
    private String content;
}
