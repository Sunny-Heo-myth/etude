package org.alan.etude.entity.comment;

import org.alan.etude.factory.entity.CommentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    void deleteTest() {
        // given
        Comment comment = CommentFactory.createComment(null);
        boolean beforeDeleted = comment.isDeleted();

        // when
        comment.delete();

        // then
        boolean afterDeleted = comment.isDeleted();
        assertThat(beforeDeleted).isFalse();
        assertThat(afterDeleted).isTrue();
    }

    @Test
    void findDeletableCommentWhenExistsTest() {
        // given

        // 1 2 3 5
        //     4

        Comment comment1 = CommentFactory.createComment(null);
        Comment comment2 = CommentFactory.createComment(comment1);
        Comment comment3 = CommentFactory.createComment(comment2);
        Comment comment4 = CommentFactory.createComment(comment2);
        Comment comment5 = CommentFactory.createComment(comment3);

        comment2.delete();
        comment3.delete();

        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3, comment4));
        ReflectionTestUtils.setField(comment3, "children", List.of(comment5));
        ReflectionTestUtils.setField(comment4, "children", List.of());
        ReflectionTestUtils.setField(comment5, "children", List.of());

        // when
        Optional<Comment> deletableComment = comment5.findDeletableComment();

        // then
        assertThat(deletableComment).containsSame(comment3);
    }

    @Test
    void findDeletableCommentWhenNotExistsTest() {
        // given

        // 1 2 3

        Comment comment1 = CommentFactory.createComment(null);
        Comment comment2 = CommentFactory.createComment(comment1);
        Comment comment3 = CommentFactory.createComment(comment2);

        ReflectionTestUtils.setField(comment1, "children", List.of(comment2));
        ReflectionTestUtils.setField(comment2, "children", List.of(comment3));
        ReflectionTestUtils.setField(comment3, "children", List.of());

        // when
        Optional<Comment> deletableComment = comment2.findDeletableComment();

        // then
        assertThat(deletableComment).isEmpty();
    }
}
