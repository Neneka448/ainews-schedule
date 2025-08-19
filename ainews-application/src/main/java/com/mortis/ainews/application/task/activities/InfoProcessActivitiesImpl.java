package com.mortis.ainews.application.task.activities;


import ai.z.openapi.service.web_search.WebSearchRequest;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.application.service.facility.ZhipuAIService;
import com.mortis.ainews.domain.activities.IInfoProcessActivities;
import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.InfoProcessMetadata;
import com.mortis.ainews.domain.model.KeywordDO;
import com.mortis.ainews.domain.model.KeywordRelatedContent;
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
    public KeywordRelatedContent fetchContent(KeywordDO keywordDO) {
        var resp = zhipuAIService.search(WebSearchRequest
            .builder()
            .searchEngine("search_pro")
            .searchQuery(keywordDO.getContent())
            .count(15)
            .searchRecencyFilter("oneWeek")
            .contentSize("high")
            .build());
        return KeywordRelatedContent
            .builder()
            .keywordDO(keywordDO)
            .contents(resp
                .stream()
                .map(item -> "<title>\n" + item.getTitle() + "</title>\n" + "<content>\n" +
                    item.getContent() + "</content>\n")
                .toList())
            .build();
    }

    @Override
    public String process(List<KeywordRelatedContent> contents, InfoProcessData processData) {
        return "";
    }

    @Override
    public void save(String processedData, List<KeywordRelatedContent> contentIds, InfoProcessData processData) {

    }

    @Override
    public void notifyUser(String info) {

    }

}
