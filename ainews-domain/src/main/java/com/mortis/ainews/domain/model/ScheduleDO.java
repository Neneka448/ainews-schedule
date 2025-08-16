package com.mortis.ainews.domain.model;

import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDO {
    private Long id;
    private String name;
    private String prompt;
    private ScheduleSpecDO spec;
    private TemporalTaskTypeEnum workflowType;
    private ScheduleStatusEnum status;
    private int deleted;

}