package com.mortis.ainews.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ScheduleStatusEnum {
    COMMIT(
        0,
        "Submitted to database but not yet started in Temporal"
    ),
    RUNNING(
        1,
        "Submitted to Temporal and executing according to plan"
    ),
    FAILED(
        2,
        "Cancelled due to error"
    ),
    COMPLETED(
        3,
        "Plan execution finished successfully"
    ),
    CANCELLED(
        4,
        "Manually cancelled"
    ),
    PAUSED(
        5,
        "Manually paused, can be resumed later"
    ),
    ;


    private final int value;
    private final String description;

    ScheduleStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static ScheduleStatusEnum fromValue(int value) {
        for (ScheduleStatusEnum status : ScheduleStatusEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
