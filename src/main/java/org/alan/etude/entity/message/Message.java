package org.alan.etude.entity.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.alan.etude.entity.common.EntityDate;
import org.alan.etude.entity.member.Member;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Message extends EntityDate {

    @Id // Message id will be created with ascending order.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    @Column(nullable = false)
    private boolean deletedBySender;

    @Column(nullable = false)
    private boolean deletedByReceiver;

    public Message(String content, Member sender, Member receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.deletedBySender = false;
        this.deletedByReceiver = false;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeletable() {
        return isDeletedBySender() && isDeletedByReceiver();
    }

}
