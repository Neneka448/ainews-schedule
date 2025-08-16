package com.mortis.ainews.application.persistence.converter;

import com.mortis.ainews.application.persistence.po.schedules.Schedule;
import com.mortis.ainews.domain.model.ScheduleDO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleConverter {

    ScheduleDO toDO(Schedule schedule);

    Schedule toPO(ScheduleDO scheduleDO);
}
