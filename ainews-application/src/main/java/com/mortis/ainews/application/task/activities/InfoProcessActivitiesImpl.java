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
import io.temporal.failure.ApplicationFailure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.ai.retry.NonTransientAiException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
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
        log.info(
            "Starting fetchContent activity for keyword: {}",
            keywordDO.getContent()
        );

        try {
            var resp = zhipuAIService.search(WebSearchRequest
                .builder()
                .searchEngine("search_pro")
                .searchQuery(keywordDO.getContent())
                .count(15)
                .searchRecencyFilter("oneWeek")
                .contentSize("high")
                .build());

            log.info(
                "Successfully fetched {} search results for keyword: {}",
                resp.size(),
                keywordDO.getContent()
            );

            return KeywordRelatedContent
                .builder()
                .keywordDO(keywordDO)
                .contents(resp
                    .stream()
                    .map(item -> "<News_item>\n<title>\n" + item.getTitle() + "</title>\n" + "<content>\n" +
                        item.getContent() + "</content>\n<publish_time>\n" + item.getPublishDate() + "\n</publish_time>\n<referer>\n" + item.getRefer() + "\n</referer>\n<media>\n" + item.getMedia() + "\n</media>\n<url>\n" + item.getLink() + "\n</url>\n</News_item>\n")
                    .toList())
                .build();
        } catch (Exception e) {
            log.error(
                "Failed to fetch content for keyword: {}",
                keywordDO.getContent(),
                e
            );
            throw e;
        }
    }

    @Override
    public String process(List<KeywordRelatedContent> contents, InfoProcessData processData) {
        try {
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
        } catch (NonTransientAiException e) {
            throw ApplicationFailure.newNonRetryableFailure(
                "AI provider client error: " + e.getMessage(),
                e
                    .getClass()
                    .getName()
            );
        } catch (RestClientResponseException e) {
            int status = e
                .getStatusCode()
                .value();
            if (status >= 400 && status < 500) {
                throw ApplicationFailure.newNonRetryableFailure(
                    "HTTP " + status + " from AI provider: " + e.getResponseBodyAsString(),
                    e
                        .getClass()
                        .getName()
                );
            }
            throw e;
        }
    }

    @Override
    public void save(String processedData, List<KeywordRelatedContent> contentIds, InfoProcessData processData) {

    }

    @Override
    public void notifyUser(String info) {

    }

}
