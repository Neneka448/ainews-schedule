package com.mortis.ainews.application.persistence.converter.facade;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.mortis.ainews.application.persistence.converter.KeywordConverter;
import com.mortis.ainews.application.persistence.converter.UserConverter;
import com.mortis.ainews.application.persistence.converter.UserSubscriptionRelConverter;
import com.mortis.ainews.application.persistence.converter.ScheduleConverter;
import com.mortis.ainews.application.persistence.converter.UserScheduleRelConverter;
import com.mortis.ainews.application.persistence.converter.KeywordScheduleRelConverter;

@Component
@RequiredArgsConstructor
public class ConverterFacade {
    public final KeywordConverter keywordConverter;
    public final UserConverter userConverter;
    public final UserSubscriptionRelConverter userSubscriptionRelConverter;
    public final ScheduleConverter scheduleConverter;
    public final UserScheduleRelConverter userScheduleRelConverter;
    public final KeywordScheduleRelConverter keywordScheduleRelConverter;
}
