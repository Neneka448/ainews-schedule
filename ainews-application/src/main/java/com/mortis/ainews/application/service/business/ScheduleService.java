package com.mortis.ainews.application.service.business;

import com.mortis.ainews.application.persistence.converter.facade.ConverterFacade;
import com.mortis.ainews.application.persistence.repository.interfaces.ScheduleRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserScheduleRepository;
import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.model.ScheduleDO;
import com.mortis.ainews.domain.model.UserScheduleRelDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final ConverterFacade converterFacade;

    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO createSchedule(ScheduleDO schedule, Long userId) {
        var res = scheduleRepository.save(converterFacade.scheduleConverter.toPO(schedule));
        userScheduleRepository.save(converterFacade.userScheduleRelConverter.toPO(UserScheduleRelDO
            .builder()
            .userId(userId)
            .scheduleId(res.getId())
            .build()
        ));
        return converterFacade.scheduleConverter.toDO(res);
    }

}
