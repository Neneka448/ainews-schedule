package com.mortis.ainews.application.task.activities;


import com.mortis.ainews.application.persistence.repository.interfaces.ScheduleRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserScheduleRepository;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.domain.activities.IInfoProcessActivities;
import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.InfoProcessMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InfoProcessActivitiesImpl implements IInfoProcessActivities {

    private final ScheduleService scheduleService;

    @Override
    public InfoProcessData fetchMetadata(Long userId, Long scheduleId) {
        var keywords = scheduleService.getKeywordsByScheduleId(scheduleId);
        var scheduleDO = scheduleService.getScheduleById(scheduleId);
        return InfoProcessData
            .builder()
            .metadata(InfoProcessMetadata
                .builder()
                .userId(userId)
                .scheduleId(scheduleId.intValue())
                .build())
            .scheduleDO(scheduleDO)
            .keywordDOList(keywords)
            .build();
    }


    @Override
    public List<String> fetchContent(InfoProcessData data) {
        return List.of();
    }

    @Override
    public String process() {
        return "";
    }

    @Override
    public String save() {
        return "";
    }

    @Override
    public String notifyUser() {
        return "";
    }
}
