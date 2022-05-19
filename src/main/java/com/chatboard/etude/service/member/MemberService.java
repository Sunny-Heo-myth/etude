package com.chatboard.etude.service.member;

import com.chatboard.etude.dto.member.MemberDto;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.exception.notFoundException.MemberNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto readMember(Long id) {
        return MemberDto.toDto(memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    @PreAuthorize("@memberGuard.check(#id)")
    public void deleteMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        // deleteById issue two different "select sql" & "delete sql".
        memberRepository.delete(member);
    }

}
