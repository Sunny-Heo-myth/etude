package com.chatboard.etude.repository.post;

import com.chatboard.etude.dto.post.PostReadCondition;
import com.chatboard.etude.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDto> findAllByCondition(PostReadCondition condition);
}
