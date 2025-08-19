package com.mortis.ainews.domain.activities;


import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.KeywordDO;
import com.mortis.ainews.domain.model.KeywordRelatedContent;
import io.temporal.activity.ActivityInterface;

import java.util.List;

@ActivityInterface
public interface IInfoProcessActivities {

    InfoProcessData fetchMetadata(Long userId, Long scheduleId);

    KeywordRelatedContent fetchContent(KeywordDO keyword);

    String process(List<KeywordRelatedContent> contents, InfoProcessData processData);

    void save(String processedData, List<KeywordRelatedContent> contentIds, InfoProcessData processData);

    void notifyUser(String info);

}
