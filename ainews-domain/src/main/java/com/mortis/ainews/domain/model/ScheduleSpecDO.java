package com.mortis.ainews.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Each field can be one of:
 * *: matches always
 * x: matches when the field equals x
 * x/y : matches when the field equals x+n*y where n is an integer
 * x-z: matches when the field is between x and z inclusive
 * w,x,y,...: matches when the field is one of the listed values
 * Each x, y, z, ... is either a decimal integer, or a month or day of week name
 * or abbreviation (in the appropriate fields).
 */
@Data
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSpecDO {
    private String second;
    private String minute;
    private String hour;
    private String day;
    /**
     * abbr of month name: jan/feb/mar/apr/may/jun/jul/aug/sep/oct/nov/dec
     * 1-12
     */
    private String month;
    private String year;
    private String dayOfMonth;
    /**
     * week name: monday/tuesday/wednesday/thursday/friday/saturday/sunday
     * 0-6/1-7均可
     */
    private String dayOfWeek;
}