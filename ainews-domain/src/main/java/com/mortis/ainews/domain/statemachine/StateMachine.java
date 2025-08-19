package com.mortis.ainews.domain.statemachine;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 通用状态机接口
 *
 * @param <S> 状态枚举类型
 */
public interface StateMachine<S extends Enum<S>> {

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    S getCurrentState();

    /**
     * 设置当前状态（内部使用）
     *
     * @param state 新状态
     */
    void setCurrentState(S state);

    /**
     * 获取最后更新时间
     *
     * @return 最后更新时间
     */
    Instant getLastUpdated();

    /**
     * 设置最后更新时间（内部使用）
     *
     * @param lastUpdated 最后更新时间
     */
    void setLastUpdated(Instant lastUpdated);

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getErrorMessage();

    /**
     * 设置错误信息（内部使用）
     *
     * @param errorMessage 错误信息
     */
    void setErrorMessage(String errorMessage);

    /**
     * 获取实体ID（用于日志，不对外序列化）
     */
    @JsonIgnore
    Object getEntityId();

    /**
     * 获取从指定状态可以转换到的有效状态集合（内部规则，不对外序列化）
     *
     * @param fromState 源状态
     * @return 可转换到的状态集合
     */
    @JsonIgnore
    Set<S> getValidTransitions(S fromState);

    /**
     * 获取失败状态（内部规则，不对外序列化）
     *
     * @return 失败状态
     */
    @JsonIgnore
    S getFailedState();

    /**
     * 获取终态集合（内部规则，不对外序列化）
     *
     * @return 终态集合
     */
    @JsonIgnore
    Set<S> getTerminalStates();
}
