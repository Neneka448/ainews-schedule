package com.mortis.ainews.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDO {
    private Long id;
    private String name;
    private String prompt;
    private ScheduleSpecDO spec;
    private int deleted;
}