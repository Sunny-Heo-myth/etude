package com.chatboard.etude.service.alarm;

import com.chatboard.etude.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsAlarmService implements AlarmService{

    @Override
    public void alarm(AlarmInfoDto alarmInfoDto) {
        log.info("Sms to = {} : {}",
                alarmInfoDto.getTargetMemberDto().getUsername(), alarmInfoDto.getMessage());
    }
}
