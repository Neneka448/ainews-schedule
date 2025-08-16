package com.mortis.ainews.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSpecDTO {
    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "秒字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String second = "0";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "分钟字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String minute = "0";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "小时字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String hour = "0";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "月份字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String month = "*";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "年份字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String year = "*";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "日期字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String dayOfMonth = "*";

    @Pattern(regexp = "^(\\*|\\d+(,\\d+)*|\\d+-\\d+|\\d+/\\d+)?$",
             message = "星期字段格式无效，支持: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)")
    private String dayOfWeek;
}