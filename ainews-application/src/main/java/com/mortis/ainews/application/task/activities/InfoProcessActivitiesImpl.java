package com.mortis.ainews.application.task.activities;


import ai.z.openapi.service.web_search.WebSearchRequest;
import com.mortis.ainews.application.persistence.repository.interfaces.ScheduleRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserScheduleRepository;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.application.service.facility.ZhipuAIService;
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
    private final ZhipuAIService zhipuAIService;

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
    public List<String> fetchContent(String content) {
        var resp = zhipuAIService.search(WebSearchRequest
            .builder()
            .searchEngine("search_pro")
            .searchQuery(content)
            .count(15)
            .searchRecencyFilter("oneWeek")
            .contentSize("high")
            .build());
        return resp
            .stream()
            .map(item -> "<title>\n" + item.getTitle() + "</title>\n" + "<content>\n" +
                item.getContent() + "</content>\n")
            .toList();
    }

    @Override
    public String process(List<String> contents, InfoProcessData processData) {
        return "";
    }

    @Override
    public String save(String processedData, List<Long> contentIds, InfoProcessData processData) {
        return "";
    }

    @Override
    public String notifyUser() {
        return "";
    }
}
