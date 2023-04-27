package org.alan.etude.entity.message;

import org.alan.etude.factory.entity.MessageFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    @Test
    void deleteBtSenderTest() {
        // given
        Message message = MessageFactory.createMessage();

        // when
        message.deleteBySender();

        // then
        assertThat(message.isDeletedBySender()).isTrue();
    }

    @Test
    void deleteBtReceiverTest() {
        // given
        Message message = MessageFactory.createMessage();

        // when
        message.deleteByReceiver();

        // then
        assertThat(message.isDeletedByReceiver()).isTrue();
    }

    @Test
    void isNotDeletableTest() {
        // given
        Message message = MessageFactory.createMessage();

        // when
        boolean deletable = message.isDeletable();

        // then
        assertThat(deletable).isFalse();
    }

    @Test
    void isDeletableTest() {
        // given
        Message message = MessageFactory.createMessage();

        // when
        boolean deletable = message.isDeletable();
        message.deleteBySender();
        message.deleteByReceiver();

        // then
        assertThat(deletable).isFalse();
    }
}
