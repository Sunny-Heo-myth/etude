package org.alan.etude.service.alarm;

import lombok.extern.slf4j.Slf4j;
import org.alan.etude.dto.alarm.AlarmInfoDto;
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
