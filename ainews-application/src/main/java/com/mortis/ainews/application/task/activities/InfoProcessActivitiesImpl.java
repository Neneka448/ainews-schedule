package com.mortis.ainews.application.task.activities;


import ai.z.openapi.service.web_search.WebSearchRequest;
import com.mortis.ainews.application.constant.PromptTemplate;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.application.service.facility.SpringAiService;
import com.mortis.ainews.application.service.facility.ZhipuAIService;
import com.mortis.ainews.domain.activities.IInfoProcessActivities;
import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.InfoProcessMetadata;
import com.mortis.ainews.domain.model.KeywordDO;
import com.mortis.ainews.domain.model.KeywordRelatedContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InfoProcessActivitiesImpl implements IInfoProcessActivities {

    private final ScheduleService scheduleService;
    private final ZhipuAIService zhipuAIService;
    private final SpringAiService springAiService;

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
                .map(item -> "<News_item>\n<title>\n" + item.getTitle() + "</title>\n" + "<content>\n" +
                    item.getContent() + "</content>\n</News_item>")
                .toList())
            .build();
    }

    @Override
    public String process(List<KeywordRelatedContent> contents, InfoProcessData processData) {
        return springAiService.chatWithTemplate(
            PromptTemplate.News_Aggregate_PromptTemplate,
            Map.ofEntries(Map.entry(
                "newsContent",
                String.join(
                    "",
                    contents
                        .stream()
                        .map(KeywordRelatedContent::getContents)
                        .flatMap(List::stream)
                        .toList()
                )
            ))
        );
    }

    @Override
    public void save(String processedData, List<KeywordRelatedContent> contentIds, InfoProcessData processData) {

    }

    @Override
    public void notifyUser(String info) {

    }

}
