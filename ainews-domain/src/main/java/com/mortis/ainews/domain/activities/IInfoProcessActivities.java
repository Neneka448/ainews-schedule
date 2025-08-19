package com.mortis.ainews.domain.activities;


import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.KeywordDO;
import com.mortis.ainews.domain.model.KeywordRelatedContent;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.List;

@ActivityInterface
public interface IInfoProcessActivities {

    @ActivityMethod(name = "FetchMetadata")
    InfoProcessData fetchMetadata(Long userId, Long scheduleId);

    @ActivityMethod(name = "FetchContent")
    KeywordRelatedContent fetchContent(KeywordDO keyword);

    @ActivityMethod(name = "Process")
    String process(List<KeywordRelatedContent> contents, InfoProcessData processData);

    @ActivityMethod(name = "Save")
    void save(String processedData, List<KeywordRelatedContent> contentIds, InfoProcessData processData);

    @ActivityMethod(name = "NotifyUser")
    void notifyUser(String info);

}
