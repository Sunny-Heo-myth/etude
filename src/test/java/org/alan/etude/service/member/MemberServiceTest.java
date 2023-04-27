package org.alan.etude.service.member;

import org.alan.etude.dto.member.MemberDto;
import org.alan.etude.entity.member.Member;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    void readTest() {
        // given
        Member member = createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberDto memberDto = memberService.readMember(1L);

        // then
        assertThat(memberDto.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.readMember(1L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        // when
        memberService.deleteMember(1L);

        // then
        verify(memberRepository).delete(any());
    }

    @Test
    void deleteExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.deleteMember(1L))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
