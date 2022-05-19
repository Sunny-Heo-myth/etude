package com.chatboard.etude.config.security.guard;

import com.chatboard.etude.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class MemberGuard extends Guard{

    public MemberGuard() {
        super();
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthUtils.extractMemberId());
    }
}
