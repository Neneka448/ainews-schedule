package com.mortis.ainews.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSpecDTO {
    private String second;
    private String minute;
    private String hour;
    private String day;
    private String month;
    private String year;
    private String dayOfMonth;
    private String dayOfWeek;
}