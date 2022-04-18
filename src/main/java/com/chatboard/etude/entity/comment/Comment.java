package com.chatboard.etude.entity.comment;

import com.chatboard.etude.dto.member.MemberDto;
import com.chatboard.etude.entity.common.EntityDate;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.event.comment.CommentCreatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    @OneToMany(mappedBy = "parent") // mappedBy always depicts name of class field.
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted;

    public Comment(String content, Member member, Post post, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
        this.deleted = false;
    }


    // if there is child comment it is not deletable.
    public Optional<Comment> findDeletableComment() {
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    // called when there is still child comment so that this comment can not be completely deleted.
    public void delete() {
        this.deleted = true;
    }

    // called recursively ascend to upper comment hierarchy until a parent comment is not deletable.
    // with default_batch_fetch_size to avoid 2*n + 1 queries.
    private Comment findDeletableCommentByParent() {
        if (isDeletableParent()) {
            Comment deletableParent = this.getParent().findDeletableCommentByParent();
            // by checking child number late, I could exploit default_batch_size with "in" clause.
            if (this.getParent().getChildren().size() == 1) {
                return deletableParent;
            }
        }
        return this;
    }

    private boolean isDeletableParent() {
        // There is a parent with this comment.
        // and "{this.deleted == true}".
        return this.getParent() != null && getParent().isDeleted();
    }

    private boolean hasChildren() {
        return this.getChildren().size() != 0;
    }

    public void publishCreatedEvent(ApplicationEventPublisher publisher) {
        publisher.publishEvent(
                new CommentCreatedEvent(
                        MemberDto.toDto(this.getMember()),
                        MemberDto.toDto(this.getPost().getMember()),
                        Optional.ofNullable(this.getParent())
                                .map(Comment::getMember)
                                .map(MemberDto::toDto)
                                .orElseGet(MemberDto::empty),   // empty memberDto value
                        getContent()
                )
        );
    }
}
