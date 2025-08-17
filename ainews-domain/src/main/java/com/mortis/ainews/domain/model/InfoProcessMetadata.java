package com.mortis.ainews.domain.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonSerialize
public class InfoProcessMetadata {
    private Long userId;
    private int scheduleId;
    private List<Long> keywordIds;
}
