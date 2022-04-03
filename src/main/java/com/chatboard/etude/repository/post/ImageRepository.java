package com.chatboard.etude.repository.post;

import com.chatboard.etude.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
