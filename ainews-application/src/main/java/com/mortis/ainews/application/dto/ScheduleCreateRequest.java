package com.mortis.ainews.application.dto;


import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Data
public class ScheduleCreateRequest {
    @NotNull(message = "工作流类型不能为空")
    private TemporalTaskTypeEnum workflowType;

    /**
     * 前端计划时间表达
     */
    @NotNull(message = "计划规格不能为空")
    @Valid
    private ScheduleSpecDTO spec;

    @NotBlank(message = "计划名称不能为空")
    @Size(max = 100, message = "计划名称长度不能超过100个字符")
    private String scheduleName;

    private String prompt;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<@Positive(message = "关键词ID必须为正数") Long> tagIds = List.of();

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Optional<Map<String, String>> workflowParams = Optional.empty();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Optional<CreateScheduleOptDTO> createScheduleOpt = Optional.empty();
}
