package org.alan.etude.factory.entity;

import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.message.Message;

import static org.alan.etude.factory.entity.MemberFactory.createMember;

public class MessageFactory {

    public static Message createMessage() {
        return new Message("content", createMember(), createMember());
    }

    public static Message createMessage(Member sender, Member receiver) {
        return new Message("content", sender, receiver);
    }

}
