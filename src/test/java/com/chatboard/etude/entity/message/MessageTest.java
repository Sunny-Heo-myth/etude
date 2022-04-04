package com.chatboard.etude.entity.message;

import org.junit.jupiter.api.Test;

import static com.chatboard.etude.factory.entity.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    @Test
    void deleteBtSenderTest() {
        // given
        Message message = createMessage();

        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeletedBySender()).isTrue();
    }

    @Test
    void deleteBtReceiverTest() {
        // given
        Message message = createMessage();

        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeletedByReceiver()).isTrue();
    }

    @Test
    void isNotDeletableTest() {
        // given
        Message message = createMessage();

        // when
        boolean deletable = message.isDeletable();

        // then
        assertThat(deletable).isFalse();
    }

    @Test
    void isDeletableTest() {
        // given
        Message message = createMessage();

        // when
        boolean deletable = message.isDeletable();
        message.deleteBySender();
        message.deleteByReceiver();

        // then
        assertThat(deletable).isFalse();
    }
}
