package com.chatboard.etude.factory.entity;

import com.chatboard.etude.entity.comment.Comment;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Post;

import static com.chatboard.etude.factory.entity.MemberFactory.createMember;
import static com.chatboard.etude.factory.entity.PostFactory.createPost;

public class CommentFactory {

    public static Comment createComment(Comment parent) {
        return new Comment("content", createMember(), createPost(), parent);
    }
    public static Comment createDeletedComment(Comment parent) {
        Comment comment = new Comment("content", createMember(), createPost(), parent);
        comment.delete();
        return comment;
    }
    public static Comment createComment(Member member, Post post, Comment parent) {
        return new Comment("content", member, post, parent);
    }
}
