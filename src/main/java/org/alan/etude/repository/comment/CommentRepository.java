package org.alan.etude.repository.comment;

import org.alan.etude.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c " +
            "left join fetch c.parent " +
            "where c.id = :id")
    Optional<Comment> findWithParentById(Long id);

    // 1. find all comments with a single query
    // 2. turn it into hierarchical structure by java
    @Query("select c " +
            "from Comment c join fetch c.member left join fetch c.parent " +
            "where c.post.id = :postId " +
            "order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllCommentWithMemberAndParentByPostId(Long postId);

    @Query("select c " +
            "from Comment c join fetch c.member " +
            "where c.post.id = :postId " +
            "order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllCommentWithMemberByPostId(Long postId);
}
