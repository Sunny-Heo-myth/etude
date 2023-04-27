package org.alan.etude.config.security.guard;

import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.repository.post.PostRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
