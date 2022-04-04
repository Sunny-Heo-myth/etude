package com.chatboard.etude.factory.entity;

import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.message.Message;

import static com.chatboard.etude.factory.entity.MemberFactory.createMember;

public class MessageFactory {

    public static Message createMessage() {
        return new Message("content", createMember(), createMember());
    }

    public static Message createMessage(Member sender, Member receiver) {
        return new Message("content", sender, receiver);
    }

}
