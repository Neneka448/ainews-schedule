package com.mortis.ainews.application.dto.converter;


import com.mortis.ainews.application.dto.ScheduleSpecDTO;
import com.mortis.ainews.domain.model.ScheduleSpecDO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleSpecConverter {
    ScheduleSpecDO toDO(ScheduleSpecDTO dto);

    List<ScheduleSpecDO> toDOs(List<ScheduleSpecDTO> dto);

    ScheduleSpecDTO toDTO(ScheduleSpecDO scheduleSpecDO);

    List<ScheduleSpecDTO> toDTOs(List<ScheduleSpecDO> scheduleSpecDOs);
}
