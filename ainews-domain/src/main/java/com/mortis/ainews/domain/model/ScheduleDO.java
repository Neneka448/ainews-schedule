package com.mortis.ainews.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import com.mortis.ainews.domain.statemachine.AbstractStateMachine;
import com.mortis.ainews.domain.statemachine.ScheduleStateMachine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class ScheduleDO extends AbstractStateMachine<ScheduleStatusEnum> implements ScheduleStateMachine {
    private Long id;
    private String name;
    private String prompt;
    private ScheduleSpecDO spec;
    private TemporalTaskTypeEnum workflowType;
    private ScheduleStatusEnum status;
    private int deleted;
    private Instant createdAt;
    private Instant lastUpdated;
    private String errorMessage;

    // ==================== StateMachine 接口实现 ====================

    @Override
    public ScheduleStatusEnum getCurrentState() {
        return this.status;
    }

    @Override
    public void setCurrentState(ScheduleStatusEnum state) {
        this.status = state;
    }

    @Override
    public Object getEntityId() {
        return this.id;
    }

    // ==================== ScheduleStateMachine 接口实现 ====================

    @Override
    public void markAsRunning() {
        transitionTo(
            ScheduleStatusEnum.RUNNING,
            null
        );
    }

    @Override
    public void markAsCompleted() {
        transitionTo(
            ScheduleStatusEnum.COMPLETED,
            null
        );
    }

    @Override
    public void markAsCancelled() {
        transitionTo(
            ScheduleStatusEnum.CANCELLED,
            null
        );
    }

    // ==================== 业务便捷方法 ====================

    /**
     * 初始化为COMMIT状态
     */
    public void initializeAsCommit() {
        this.status = ScheduleStatusEnum.COMMIT;
        // createdAt 和 lastUpdated 由数据库自动设置
    }

    /**
     * 重试失败的Schedule
     * 只有FAILED状态的Schedule才能重试
     */
    public void retry() {
        if (!canRetry()) {
            throw new IllegalStateException(
                String.format(
                    "Schedule %d cannot be retried, current status: %s",
                    this.id,
                    this.status
                ));
        }
        markAsRunning();
    }

}