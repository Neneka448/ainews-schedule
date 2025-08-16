package com.mortis.ainews.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordScheduleRelDO {
    private Long keywordId;
    private Long scheduleId;
    private int deleted;
}
