package org.alan.etude.service.alarm;

import lombok.extern.slf4j.Slf4j;
import org.alan.etude.dto.alarm.AlarmInfoDto;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailAlarmService implements AlarmService{

    @Override
    public void alarm(AlarmInfoDto alarmInfoDto) {
        log.info("Emailed to = {} : {}",
                alarmInfoDto.getTargetMemberDto().getEmail(), alarmInfoDto.getMessage());
    }
}
