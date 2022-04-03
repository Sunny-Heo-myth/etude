package com.chatboard.etude.factory.entity;

import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Image;
import com.chatboard.etude.entity.post.Post;

import java.util.List;

import static com.chatboard.etude.factory.entity.CategoryFactory.createCategory;
import static com.chatboard.etude.factory.entity.MemberFactory.createMember;

public class PostFactory {

    public static Post createPost() {
        return createPost(createMember(), createCategory());
    }

    public static Post createPost(Member member, Category category) {
        return new Post("title", "content", 1000L, member, category, List.of());
    }

    public static Post createPostWithImages(Member member, Category category, List<Image> images) {
        return new Post("title", "content", 1000L, member, category, images);
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", 1000L, createMember(), createCategory(), images);
    }
}
