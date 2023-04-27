package org.alan.etude.factory.entity;

import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;

public class CommentFactory {

    public static Comment createComment(Comment parent) {
        return new Comment("content", MemberFactory.createMember(), PostFactory.createPost(), parent);
    }
    public static Comment createDeletedComment(Comment parent) {
        Comment comment = new Comment("content", MemberFactory.createMember(), PostFactory.createPost(), parent);
        comment.delete();
        return comment;
    }
    public static Comment createComment(Member member, Post post, Comment parent) {
        return new Comment("content", member, post, parent);
    }
}
