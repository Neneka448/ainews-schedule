package com.mortis.ainews.application.persistence.converter;

import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleId;
import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleRel;
import com.mortis.ainews.domain.model.KeywordScheduleRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KeywordScheduleRelConverter {

    @Mapping(target = "keywordId", source = "id.keywordId")
    @Mapping(target = "scheduleId", source = "id.scheduleId")
    KeywordScheduleRelDO toKeywordScheduleRelDO(KeywordScheduleRel keywordScheduleRel);

    @Mapping(target = "id", expression = "java(new KeywordScheduleId(keywordScheduleRelDO.getKeywordId(), keywordScheduleRelDO.getScheduleId()))")
    KeywordScheduleRel toKeywordScheduleRel(KeywordScheduleRelDO keywordScheduleRelDO);
}
