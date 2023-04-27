package org.alan.etude.repository.message;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.dto.message.MessageSimpleDto;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.message.Message;
import org.alan.etude.exception.notFoundException.MessageNotFoundException;
import org.alan.etude.factory.entity.MemberFactory;
import org.alan.etude.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.alan.etude.factory.entity.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager entityManager;

    Member sender, receiver;

    @BeforeEach
    void beforeEach() {
        sender = memberRepository.save(MemberFactory.createMember("sender@sender.com", "sender", "sender", "sender"));
        receiver = memberRepository.save(MemberFactory.createMember("receiver@receiver.com", "receiver", "receiver", "receiver"));
    }

    private void clear() {
        entityManager.flush();  // synchronized
        entityManager.clear();  // clear persistence context
    }
    @Test
    void createAndReadTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        Message foundMessage = messageRepository.findById(message.getId())
                .orElseThrow(MessageNotFoundException::new);

        // then
        assertThat(foundMessage.getId()).isEqualTo(message.getId());
    }

    @Test
    void deleteTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));

        // when
        messageRepository.delete(message);

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void deleteCascadeBySenderTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        memberRepository.deleteById(sender.getId());
        clear();

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByReceiverTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        memberRepository.deleteById(receiver.getId());
        clear();

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void findWithSenderAndReceiverByIdTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        Message foundMessage = messageRepository.findMessageWithSenderAndReceiverById(message.getId())
                .orElseThrow(MessageNotFoundException::new);

        // then
        assertThat(foundMessage.getId()).isEqualTo(message.getId());
        assertThat(foundMessage.getSender().getEmail()).isEqualTo(sender.getEmail());
        assertThat(foundMessage.getReceiver().getEmail()).isEqualTo(receiver.getEmail());
    }

    @Test
    void findAllBySenderIdOrderByMessageIdDescTest() {
        // given
        List<Message> messages = IntStream.range(0, 4)
                .mapToObj(num -> messageRepository.save(createMessage(sender, receiver)))
                .collect(Collectors.toList());

        messages.get(2).deleteBySender();
        final int size = 2;
        clear();

        // when
        Slice<MessageSimpleDto> result1 = messageRepository.findAllMessageBySenderIdOrderByMessageIdDesc(
                sender.getId(), Long.MAX_VALUE, Pageable.ofSize(size));

        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllMessageBySenderIdOrderByMessageIdDesc(
                sender.getId(), lastMessageId1, Pageable.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        // then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());
    }

    @Test
    void findAllByReceiverIdOrderByMessageIdDescTest() {
        // given
        List<Message> messages = IntStream.range(0, 4)
                .mapToObj(num -> messageRepository.save(createMessage(sender, receiver)))
                .collect(Collectors.toList());

        messages.get(2).deleteByReceiver();
        final int size = 2;
        clear();

        // when
        Slice<MessageSimpleDto> result1 = messageRepository.findAllMessageByReceiverIdOrderByMessageIdDesc(
                receiver.getId(), Long.MAX_VALUE, Pageable.ofSize(size));
        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllMessageByReceiverIdOrderByMessageIdDesc(
                receiver.getId(), lastMessageId1, Pageable.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        // then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());

    }

}
