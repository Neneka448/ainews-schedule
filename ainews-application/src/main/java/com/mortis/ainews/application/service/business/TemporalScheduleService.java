package com.mortis.ainews.application.service.business;


import com.mortis.ainews.application.dto.CreateScheduleOptDTO;
import com.mortis.ainews.application.helper.TemporalWorkflowMappingHelper;
import com.mortis.ainews.domain.model.ScheduleDO;
import com.mortis.ainews.domain.model.ScheduleSpecDO;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mortis.ainews.domain.model.InfoProcessWorkflowParams;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporalScheduleService {
    private final ScheduleClient scheduleClient;
    private final TemporalWorkflowMappingHelper temporalWorkflowMappingHelper;


    public void createSchedule(
        Long userId, ScheduleDO scheduleDO, Map<String, String> workflowParams,
        Optional<CreateScheduleOptDTO> opt)
    {
        ScheduleSpec spec = ScheduleSpec
            .newBuilder()
            .setCalendars(
                List.of(
                    getSpecByUserScheduleSpec(scheduleDO.getSpec())
                )
            )
            .setTimeZoneName("Asia/Shanghai")
            .build();

        var schedule = Schedule
            .newBuilder()
            .setAction(
                ScheduleActionStartWorkflow
                    .newBuilder()
                    .setWorkflowType(
                        temporalWorkflowMappingHelper
                            .getInterfaceClassByType(
                                scheduleDO.getWorkflowType()
                            )
                            .orElseThrow()
                    )
                    .setOptions(
                        WorkflowOptions
                            .newBuilder()
                            .setWorkflowId(
                                genWorkflowId(
                                    userId,
                                    scheduleDO
                                ))
                            .setTaskQueue(
                                temporalWorkflowMappingHelper.getTaskQueueByType(
                                    scheduleDO.getWorkflowType()
                                )
                            )
                            .build()
                    )
                    // 将参数改为工作流入参对象，避免类型不匹配
                    .setArguments(new InfoProcessWorkflowParams(
                        userId,
                        scheduleDO.getId()
                    ))
                    .build()
            )
            .setSpec(spec)
            .build();

        var optionsBuilder = ScheduleOptions
            .newBuilder();
        if (opt.isPresent()) {

            if (opt
                .get()
                .getImmediate()
                .orElse(false))
            {
                optionsBuilder.setTriggerImmediately(true);
            }
        }

        ScheduleHandle handle = scheduleClient.createSchedule(
            scheduleDO
                .getId()
                .toString(),
            schedule,
            optionsBuilder
                .build()
        );
        log.info(
            "Created schedule with id: {}, workflowId: {}",
            handle.getId(),
            genWorkflowId(
                userId,
                scheduleDO
            )
        );
    }

    private String genWorkflowId(Long userId, ScheduleDO schedule) {
        return "user_" + userId + "_schedule_" + schedule.getId();
    }

    private ScheduleCalendarSpec getSpecByUserScheduleSpec(ScheduleSpecDO scheduleSpecDO) {
        var builder = ScheduleCalendarSpec.newBuilder();
        if (scheduleSpecDO.getSecond() != null) {
            List<ScheduleRange> secondRanges = parseSpecItem(
                "second",
                scheduleSpecDO.getSecond()
            );
            builder.setSeconds(secondRanges);
        }
        if (scheduleSpecDO.getMinute() != null) {
            List<ScheduleRange> minuteRanges = parseSpecItem(
                "minute",
                scheduleSpecDO.getMinute()
            );
            builder.setMinutes(minuteRanges);
        }
        if (scheduleSpecDO.getHour() != null) {
            List<ScheduleRange> hourRanges = parseSpecItem(
                "hour",
                scheduleSpecDO.getHour()
            );
            builder.setHour(hourRanges);
        }
        if (scheduleSpecDO.getDayOfMonth() != null) {
            List<ScheduleRange> dayOfMonthRanges = parseSpecItem(
                "dayOfMonth",
                scheduleSpecDO.getDayOfMonth()
            );
            builder.setDayOfMonth(dayOfMonthRanges);
        }
        if (scheduleSpecDO.getMonth() != null) {
            List<ScheduleRange> monthRanges = parseSpecItem(
                "month",
                scheduleSpecDO.getMonth()
            );
            builder.setMonth(monthRanges);
        }
        if (scheduleSpecDO.getYear() != null) {
            List<ScheduleRange> yearRanges = parseSpecItem(
                "year",
                scheduleSpecDO.getYear()
            );
            builder.setYear(yearRanges);
        }
        if (scheduleSpecDO.getDayOfWeek() != null) {
            List<ScheduleRange> dayOfWeekRanges = parseSpecItem(
                "dayOfWeek",
                scheduleSpecDO.getDayOfWeek()
            );
            builder.setDayOfWeek(dayOfWeekRanges);
        }

        return builder.build();
    }

    /**
     * * *: matches always
     * * x: matches when the field equals x
     * * x/y : matches when the field equals x+n*y where n is an integer
     * * x-z: matches when the field is between x and z inclusive
     * * w,x,y,...: matches when the field is one of the listed values
     * 时分秒默认为0，其他默认为 every
     */
    private List<ScheduleRange> parseSpecItem(String type, String specItem) {
        int defaultStart = getDefaultStart(type);
        int defaultEnd = getDefaultEnd(type);

        if (specItem.equals("*")) {
            return List.of(new ScheduleRange(
                defaultStart,
                defaultEnd,
                1
            ));
        }
        if (specItem.matches(",?\\d+,?")) {
            return (Arrays
                .stream(specItem.split(","))
                .filter(item -> !item.isEmpty())
                .map(item -> new ScheduleRange(Integer.parseInt(item)))
                .toList());
        }
        if (specItem.matches("^\\d+-\\d+$")) {
            String[] parts = specItem.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid range format: " + specItem);
            }
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start < defaultStart || end > defaultEnd) {
                throw new IllegalArgumentException("Range out of bounds: " + specItem);
            }
            return List.of(new ScheduleRange(
                start,
                end,
                1
            ));
        }
        if (specItem.matches("^\\d+/\\d+$")) {
            String[] parts = specItem.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid step format: " + specItem);
            }
            int start = Integer.parseInt(parts[0]);
            int step = Integer.parseInt(parts[1]);
            if (start < defaultStart || start > defaultEnd || step <= 0) {
                throw new IllegalArgumentException("Step out of bounds: " + specItem);
            }
            return List.of(new ScheduleRange(
                start,
                defaultEnd,
                step
            ));
        }

        throw new IllegalArgumentException("Invalid cron format: " + specItem);

    }

    private static int getDefaultStart(String type) {
        return switch (type) {
            case "second", "minute", "hour" -> 0;
            case "dayOfMonth", "month" -> 1;
            case "year" -> 2000;
            case "dayOfWeek" -> 0; // 0-6
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }

    private static int getDefaultEnd(String type) {
        return switch (type) {
            case "second", "minute" -> 59;
            case "hour" -> 23;
            case "dayOfMonth" -> 31;
            case "month" -> 12;
            case "year" -> 2100;
            case "dayOfWeek" -> 6;
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }


}
