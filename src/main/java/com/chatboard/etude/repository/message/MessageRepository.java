package com.chatboard.etude.repository.message;

import com.chatboard.etude.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

    //@Query("select new ")
}
