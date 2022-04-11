package com.chatboard.etude.event.comment;

import com.chatboard.etude.dto.alarm.AlarmInfoDto;
import com.chatboard.etude.dto.member.MemberDto;
import com.chatboard.etude.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCreatedListener {

    private final AlarmService emailAlarmService;
    private final AlarmService lineAlarmService;
    private final AlarmService smsAlarmService;
    private final List<AlarmService> alarmServices = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        alarmServices.add(emailAlarmService);
        alarmServices.add(lineAlarmService);
        alarmServices.add(smsAlarmService);
    }



    // After transaction committed, new listener transaction will run.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async  // listener logic will be executed in different thread, independent of responding for the core-concern.
    public void handleAlarm(CommentCreatedEvent event) {

        log.info("CommentCreatedListener.handleAlarm");
        String message = generateAlarmMessage(event);

        if (isAbleToSendToPostWriter(event)) {
            alarmTo(event.getPostWriter(), message);
        }
        if (isAbleToSendToParentWriter(event)) {
            alarmTo(event.getParentWriter(), message);
        }
    }

    private String generateAlarmMessage(CommentCreatedEvent event) {
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }

    private void alarmTo(MemberDto memberDto, String message) {
        alarmServices
                .forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(memberDto, message)));
    }

    private boolean isAbleToSendToPostWriter(CommentCreatedEvent event) {
        // when the post and comment is written by same person.
        if (!isNotSameMember(event.getPublisher(), event.getPostWriter())) {
            return false;
        }

        if (!hasParent(event)) {
            return true;
        }
        // if parent comment is written by myself.
        return isNotSameMember(event.getPostWriter(), event.getParentWriter());
    }

    private boolean isAbleToSendToParentWriter(CommentCreatedEvent event) {
        return hasParent(event) && isNotSameMember(event.getPublisher(), event.getParentWriter());
    }

    private boolean isNotSameMember(MemberDto a, MemberDto b) {
        return !Objects.equals(a.getId(), b.getId());
    }

    private boolean hasParent(CommentCreatedEvent event) {
        return event.getParentWriter().getId() != null;
    }

}
