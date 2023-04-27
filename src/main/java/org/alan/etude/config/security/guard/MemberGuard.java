package org.alan.etude.config.security.guard;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
