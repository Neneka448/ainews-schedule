package com.mortis.ainews.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScheduleRelDO {
    private Long userId;
    private Long scheduleId;
    private int deleted;

}