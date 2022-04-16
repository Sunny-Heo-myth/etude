package com.chatboard.etude.repository.message;

import com.chatboard.etude.dto.message.MessageSimpleDto;
import com.chatboard.etude.entity.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// MessageId has an incremental order.
// Paging operation with Scroll event does not need to know the total page number, so it does not count.
// Slice always select +1 element of the designated number of the element which let us know the existence of the next page.
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m " +
            "from Message m left join fetch m.sender left join fetch m.receiver " +
            "where m.id = :id")
    Optional<Message> findWithSenderAndReceiverById(Long id);

    @Query("select new com.chatboard.etude.dto.message.MessageSimpleDto(m.id, m.content, m.receiver.nickname, m.createdAt) "
    + "from Message m left join m.receiver "
    + "where m.sender.id = :senderId and m.id < :lastMessageId and m.deletedBySender = false "
    + "order by m.id desc")
    Slice<MessageSimpleDto> findAllBySenderIdOrderByMessageIdDesc(Long senderId, Long lastMessageId, Pageable pageable);

    @Query("select new com.chatboard.etude.dto.message.MessageSimpleDto(m.id, m.content, m.sender.nickname, m.createdAt) "
    + "from Message m left join m.sender "
    + "where m.receiver.id = :receiverId and m.id < :lastMessageId and m.deletedByReceiver = false "
    + "order by m.id desc")
    Slice<MessageSimpleDto> findAllByReceiverIdOrderByMessageIdDesc(Long receiverId, Long lastMessageId, Pageable pageable);

}
