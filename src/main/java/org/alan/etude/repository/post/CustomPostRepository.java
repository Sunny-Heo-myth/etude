package org.alan.etude.repository.post;

import org.alan.etude.dto.post.PostReadConditionDto;
import org.alan.etude.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {

    Page<PostSimpleDto> findAllByCondition(PostReadConditionDto condition);
}
