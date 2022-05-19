package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class PostGuard extends Guard{

    private final PostRepository postRepository;

    public PostGuard(PostRepository postRepository) {
        super();
        this.postRepository = postRepository;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return postRepository.findById(id)
                .map(Post::getMember)
                .map(Member::getId)
                .filter(memberId -> memberId.equals(AuthUtils.extractMemberId()))
                .isPresent();
    }

}
