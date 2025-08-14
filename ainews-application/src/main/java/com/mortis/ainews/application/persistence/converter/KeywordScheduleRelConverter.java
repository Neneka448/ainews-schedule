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

    @Mapping(target = "id.keywordId", source = "keywordId")
    @Mapping(target = "id.scheduleId", source = "scheduleId")
    KeywordScheduleRel toKeywordScheduleRel(KeywordScheduleRelDO keywordScheduleRelDO);

    default KeywordScheduleId createKeywordScheduleId(Long keywordId, Long scheduleId) {
        if (keywordId == null || scheduleId == null) {
            return null;
        }
        return new KeywordScheduleId(
                keywordId,
                scheduleId
        );
    }
}
