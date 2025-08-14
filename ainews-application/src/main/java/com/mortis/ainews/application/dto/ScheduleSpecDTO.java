package com.mortis.ainews.application.dto;

import lombok.Data;

@Data
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