package com.chatboard.etude.service.member;

import com.chatboard.etude.dto.member.MemberDto;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        return MemberDto.toDto(memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        if (notExistsMember(id)) {
            throw new MemberNotFoundException();
        }
        memberRepository.deleteById(id);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }
}
