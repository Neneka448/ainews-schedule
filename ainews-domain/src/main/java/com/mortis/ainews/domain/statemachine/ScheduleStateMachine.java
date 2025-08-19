package com.mortis.ainews.domain.statemachine;

import com.mortis.ainews.domain.enums.ScheduleStatusEnum;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Schedule 状态机实现
 * 定义 Schedule 的状态转换规则和业务方法
 */
public interface ScheduleStateMachine extends StateMachine<ScheduleStatusEnum> {

    /**
     * Schedule 状态转换规则
     */
    Map<ScheduleStatusEnum, Set<ScheduleStatusEnum>> SCHEDULE_TRANSITIONS = Map.of(
        ScheduleStatusEnum.COMMIT,
        Set.of(
            ScheduleStatusEnum.RUNNING,
            ScheduleStatusEnum.FAILED
        ),
        ScheduleStatusEnum.RUNNING,
        Set.of(
            ScheduleStatusEnum.COMPLETED,
            ScheduleStatusEnum.FAILED,
            ScheduleStatusEnum.CANCELLED
        ),
        ScheduleStatusEnum.FAILED,
        Set.of(ScheduleStatusEnum.RUNNING),
        // 允许重试
        ScheduleStatusEnum.COMPLETED,
        Set.of(),
        // 终态
        ScheduleStatusEnum.CANCELLED,
        Set.of()
        // 终态
    );

    /**
     * 终态集合
     */
    Set<ScheduleStatusEnum> TERMINAL_STATES = Set.of(
        ScheduleStatusEnum.COMPLETED,
        ScheduleStatusEnum.CANCELLED
    );

    @Override
    @JsonIgnore
    default Set<ScheduleStatusEnum> getValidTransitions(ScheduleStatusEnum fromState) {
        return SCHEDULE_TRANSITIONS.getOrDefault(
            fromState,
            Set.of()
        );
    }

    @Override
    @JsonIgnore
    default ScheduleStatusEnum getFailedState() {
        return ScheduleStatusEnum.FAILED;
    }

    @Override
    @JsonIgnore
    default Set<ScheduleStatusEnum> getTerminalStates() {
        return TERMINAL_STATES;
    }

    /**
     * 转换到RUNNING状态
     */
    void markAsRunning();

    /**
     * 转换到COMPLETED状态
     */
    void markAsCompleted();

    /**
     * 转换到CANCELLED状态
     */
    void markAsCancelled();

    /**
     * 检查是否正在运行
     */
    @JsonIgnore
    default boolean isRunning() {
        return getCurrentState() == ScheduleStatusEnum.RUNNING;
    }

    /**
     * 检查是否已提交但未开始
     */
    @JsonIgnore
    default boolean isPending() {
        return getCurrentState() == ScheduleStatusEnum.COMMIT;
    }

    /**
     * 检查是否已完成
     */
    @JsonIgnore
    default boolean isCompleted() {
        return getCurrentState() == ScheduleStatusEnum.COMPLETED;
    }

    /**
     * 检查是否已取消
     */
    @JsonIgnore
    default boolean isCancelled() {
        return getCurrentState() == ScheduleStatusEnum.CANCELLED;
    }

    /**
     * 检查是否失败
     */
    @JsonIgnore
    default boolean isFailed() {
        return getCurrentState() == ScheduleStatusEnum.FAILED;
    }
}
