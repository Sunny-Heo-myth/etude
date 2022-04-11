package com.chatboard.etude.repository.post;

import com.chatboard.etude.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p " +
            "from Post p join fetch p.member " +
            "where p.id = :id")
    Optional<Post> findByIdWithMember(Long id);
}
