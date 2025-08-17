package com.mortis.ainews.application.controller;

import com.mortis.ainews.application.dto.ApiResponse;
import com.mortis.ainews.application.dto.ScheduleCreateRequest;
import com.mortis.ainews.application.dto.converter.ScheduleSpecConverter;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.application.service.business.ScheduleStateService;
import com.mortis.ainews.application.service.business.ScheduleValidationService;
import com.mortis.ainews.application.service.business.TemporalScheduleService;
import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.model.ScheduleDO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleStateService scheduleStateService;
    private final TemporalScheduleService temporalScheduleService;
    private final ScheduleSpecConverter scheduleSpecConverter;
    private final ScheduleValidationService scheduleValidationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createSchedule(@Valid @RequestBody ScheduleCreateRequest req) {

        // 执行业务验证
        scheduleValidationService.validateScheduleCreateRequest(req);

        log.info(
            "Creating schedule for user: {}, workflowType: {}, name: {}",
            req.getUserId(),
            req.getWorkflowType(),
            req.getScheduleName()
        );

        var schedule = scheduleService.createSchedule(
            ScheduleDO
                .builder()
                .name(req.getScheduleName())
                .prompt(req.getPrompt())
                .spec(scheduleSpecConverter.toDO(req.getSpec()))
                .workflowType(req.getWorkflowType())
                .status(ScheduleStatusEnum.COMMIT)
                .build(),
            req.getUserId()
        );

        try {
            var params = new java.util.HashMap<>(req
                .getWorkflowParams()
                .orElse(Map.of()));
            params.put(
                "userId",
                String.valueOf(req.getUserId())
            );
            params.put(
                "scheduleId",
                String.valueOf(schedule.getId())
            );

            // 创建 Temporal 计划
            temporalScheduleService.createSchedule(
                req.getUserId(),
                schedule,
                params
                ,
                req.getCreateScheduleOpt()
            );

            // 成功后更新状态为 RUNNING
            scheduleStateService.markScheduleAsRunning(schedule.getId());

            log.info(
                "Successfully created and started schedule with id: {} for user: {}",
                schedule.getId(),
                req.getUserId()
            );

            return ResponseEntity.ok(ApiResponse.success(schedule.getId()));

        } catch (Exception e) {
            // 失败时更新状态为 FAILED
            scheduleStateService.markScheduleAsFailed(
                schedule.getId(),
                e.getMessage()
            );

            log.error(
                "Failed to create Temporal schedule for id: {}, error: {}",
                schedule.getId(),
                e.getMessage()
            );

            throw new RuntimeException(
                "计划创建失败: " + e.getMessage(),
                e
            );
        }

    }


}
