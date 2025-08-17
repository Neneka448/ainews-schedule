package com.mortis.ainews.domain.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonSerialize
public class InfoProcessData {
    private InfoProcessMetadata metadata;
    private ScheduleDO scheduleDO;
    private List<KeywordDO> keywordDOList;
}
