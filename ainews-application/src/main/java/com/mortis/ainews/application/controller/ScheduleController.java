package com.mortis.ainews.application.controller;


import com.mortis.ainews.application.dto.ApiResponse;
import com.mortis.ainews.application.dto.ScheduleCreateRequest;
import com.mortis.ainews.application.dto.converter.ScheduleSpecConverter;
import com.mortis.ainews.application.service.business.ScheduleService;
import com.mortis.ainews.application.service.business.ScheduleValidationService;
import com.mortis.ainews.application.service.business.TemporalScheduleService;
import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.model.ScheduleDO;
import io.temporal.client.schedules.ScheduleClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final TemporalScheduleService temporalScheduleService;
    private final ScheduleSpecConverter scheduleSpecConverter;
    private final ScheduleValidationService scheduleValidationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createSchedule(@Valid @RequestBody ScheduleCreateRequest req) {

        // 执行业务验证
        scheduleValidationService.validateScheduleCreateRequest(req);

        log.info("Creating schedule for user: {}, workflowType: {}, name: {}",
                req.getUserId(), req.getWorkflowType(), req.getScheduleName());

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

        temporalScheduleService.createSchedule(
            req.getUserId(),
            schedule,
            req.getWorkflowParams(),
            req.getCreateScheduleOpt()
        );

        log.info("Successfully created schedule with id: {} for user: {}",
                schedule.getId(), req.getUserId());

        return ResponseEntity.ok(ApiResponse.success(schedule.getId()));

    }


}
