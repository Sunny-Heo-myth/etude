package org.alan.etude.event.comment;

import org.alan.etude.dto.alarm.AlarmInfoDto;
import org.alan.etude.dto.member.MemberDto;
import org.alan.etude.factory.entity.MemberFactory;
import org.alan.etude.service.alarm.AlarmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
@Commit // listener acts after commit, preventing rollback for test configuration.
public class CommentCreatedListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;
    @MockBean(name = "smsAlarmService")
    AlarmService smsAlarmService;
    @MockBean(name = "emailAlarmService")
    AlarmService emailAlarmService;
    @MockBean(name = "lineAlarmService")
    AlarmService lineAlarmService;

    int calledCount;

    @AfterTransaction
    void afterEach() {
        verify(emailAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(lineAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(smsAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
    }

    @Test
    void handleCommentCreatedEventTest() {
        // given
        MemberDto publisher = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(MemberFactory.createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(MemberFactory.createMemberWithId(3L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 2;
    }

    @Test
    void handleCommentCreatedEventWhenPublisherIsPostWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        MemberDto parentWriter = MemberDto.emptyMemberDto();
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 0;
    }

    @Test
    void handleCommentCreatedEventWhenPublisherIsParentWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(MemberFactory.createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 1;
    }

    @Test
    void handleCommentCreatedEventWhenPostWriterIsParentWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(MemberFactory.createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(MemberFactory.createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(MemberFactory.createMemberWithId(2L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 1;
    }

    @Test
    void commentEventRollbackTest() {

    }
}
