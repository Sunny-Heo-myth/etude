package org.alan.etude.factory.entity;

import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Image;
import org.alan.etude.entity.post.Post;

import java.util.List;

public class PostFactory {

    public static Post createPost() {
        return createPost(MemberFactory.createMember(), CategoryFactory.createCategory());
    }

    public static Post createPost(Member member, Category category) {
        return new Post("title", "content", 1000L, member, category, List.of());
    }

    public static Post createPostWithImages(Member member, Category category, List<Image> images) {
        return new Post("title", "content", 1000L, member, category, images);
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", 1000L, MemberFactory.createMember(), CategoryFactory.createCategory(), images);
    }
}
