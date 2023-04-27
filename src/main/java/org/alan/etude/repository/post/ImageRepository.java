package org.alan.etude.repository.post;

import org.alan.etude.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
