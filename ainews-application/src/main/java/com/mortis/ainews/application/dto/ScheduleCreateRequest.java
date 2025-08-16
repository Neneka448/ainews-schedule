package com.mortis.ainews.application.dto;


import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

    private List<@Positive(message = "标签ID必须为正数") Long> tagIds;

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    private Map<String, String> workflowParams;

    private Optional<CreateScheduleOptDTO> createScheduleOpt;
}
