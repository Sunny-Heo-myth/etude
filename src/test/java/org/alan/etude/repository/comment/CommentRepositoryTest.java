package org.alan.etude.repository.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.CommentNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.alan.etude.factory.entity.CategoryFactory.createCategory;
import static org.alan.etude.factory.entity.CommentFactory.createComment;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class CommentRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @PersistenceContext
    EntityManager entityManager;

    Member member;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
        post = postRepository.save(createPost(member, category));
    }

    void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createAndReadTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        Comment foundComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    void deleteTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        commentRepository.deleteById(comment.getId());

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByPostTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByParentTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        commentRepository.deleteById(parent.getId());
        clear();

        // then
        assertThat(commentRepository.findById(child.getId())).isEmpty();
    }

    @Test
    void getChildrenTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        commentRepository.save(createComment(member, post, parent));
        commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findById(parent.getId())
                .orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getChildren().size()).isEqualTo(2);
    }

    @Test
    void findWithParentByIdTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(child.getId())
                .orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getParent()).isNotNull();
    }

    @Test
    void deleteCommentTest() {
        // given

        // 1 - 2 - 3 - 5
        //       - 4

        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment2));
        Comment comment5 = commentRepository.save(createComment(member, post, comment3));

        comment2.delete();
        comment3.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId())
                .orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment()
                .ifPresentOrElse(
                        tempComment -> commentRepository.delete(tempComment),
                        comment5::delete
                );
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        assertThat(commentIds.size()).isEqualTo(3);
        assertThat(commentIds).contains(comment1.getId(), comment2.getId(), comment4.getId());
    }

    @Test
    void deleteCommentQueryLogTest() {
        // given

        // 1 - 2 - 3 - 4 - 5

        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment3));
        Comment comment5 = commentRepository.save(createComment(member, post, comment4));
        comment1.delete();
        comment2.delete();
        comment3.delete();
        comment4.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId())
                .orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment()
                .ifPresentOrElse(
                        tempComment -> commentRepository.delete(tempComment),
                        comment5::delete
                );
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        assertThat(commentIds.size()).isEqualTo(0);
    }

    @Test
    void findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAscTest() {
        // given

        // 1 - 2 - 4 - 6
        //       - 5
        //   - 3 - 7
        // 8
        Comment c1 = commentRepository.save(createComment(member, post, null));
        Comment c2 = commentRepository.save(createComment(member, post, c1));
        Comment c3 = commentRepository.save(createComment(member, post, c1));
        Comment c4 = commentRepository.save(createComment(member, post, c2));
        Comment c5 = commentRepository.save(createComment(member, post, c2));
        Comment c6 = commentRepository.save(createComment(member, post, c4));
        Comment c7 = commentRepository.save(createComment(member, post, c3));
        Comment c8 = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        List<Comment> result = commentRepository
                .findAllCommentWithMemberAndParentByPostId(post.getId());

        // then
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getId()).isEqualTo(c1.getId());
        assertThat(result.get(1).getId()).isEqualTo(c8.getId());
        assertThat(result.get(2).getId()).isEqualTo(c2.getId());
        assertThat(result.get(3).getId()).isEqualTo(c3.getId());
        assertThat(result.get(4).getId()).isEqualTo(c4.getId());
        assertThat(result.get(5).getId()).isEqualTo(c5.getId());
        assertThat(result.get(6).getId()).isEqualTo(c7.getId());
        assertThat(result.get(7).getId()).isEqualTo(c6.getId());
    }

    @Test
    void findAllCommentWithMemberByPostIdTest() {
        // given
        Comment c1 = commentRepository.save(createComment(member, post, null));
        Comment c2 = commentRepository.save(createComment(member, post, c1));
        Comment c3 = commentRepository.save(createComment(member, post, c1));
        Comment c4 = commentRepository.save(createComment(member, post, c2));
        Comment c5 = commentRepository.save(createComment(member, post, c2));
        Comment c6 = commentRepository.save(createComment(member, post, c4));
        Comment c7 = commentRepository.save(createComment(member, post, c3));
        Comment c8 = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        List<Comment> result = commentRepository
                .findAllCommentWithMemberByPostId(post.getId());

        // then
        assertThat(result.size()).isEqualTo(8);
    }
}
