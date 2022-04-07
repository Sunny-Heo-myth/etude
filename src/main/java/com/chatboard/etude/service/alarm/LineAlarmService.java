package com.chatboard.etude.service.alarm;

import com.chatboard.etude.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LineAlarmService implements AlarmService{

    @Override
    public void alarm(AlarmInfoDto alarmInfoDto) {
        log.info("Line message to = {} : {}",
                alarmInfoDto.getTargetMemberDto().getNickname(), alarmInfoDto.getMessage());
    }
}
