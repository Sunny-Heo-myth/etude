package com.chatboard.etude.repository.post;

import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {

    Page<PostSimpleDto> findAllByCondition(PostReadConditionDto condition);
}
