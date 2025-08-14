package com.mortis.ainews.application.dto;


import com.mortis.ainews.application.enums.TemporalTaskTypeEnum;
import lombok.Data;

import java.util.List;


@Data
public class ScheduleCreateRequest {
    private TemporalTaskTypeEnum workflowType;
    /**
     * 前端计划时间表达
     */
    private ScheduleSpecDTO spec;
    private String scheduleName;
    private String prompt;
    private List<Long> tagIds;
    private Long userId;
}
