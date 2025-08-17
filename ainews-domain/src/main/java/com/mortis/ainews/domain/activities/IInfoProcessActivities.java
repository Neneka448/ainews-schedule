package com.mortis.ainews.domain.activities;


import com.mortis.ainews.domain.model.InfoProcessData;
import com.mortis.ainews.domain.model.InfoProcessMetadata;
import io.temporal.activity.ActivityInterface;

import java.util.List;

@ActivityInterface
public interface IInfoProcessActivities {

    InfoProcessData fetchMetadata(Long userId, Long scheduleId);

    List<String> fetchContent(String content);

    String process(List<String> contents, InfoProcessData processData);

    String save(String processedData, List<Long> contentIds, InfoProcessData processData);

    String notifyUser();
}
