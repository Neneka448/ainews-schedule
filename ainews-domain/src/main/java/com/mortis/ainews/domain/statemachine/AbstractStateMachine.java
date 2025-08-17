package com.mortis.ainews.domain.statemachine;

import java.time.Instant;
import java.util.Set;

/**
 * 抽象状态机实现
 * 提供通用的状态转换逻辑
 * 
 * @param <S> 状态枚举类型
 */
public abstract class AbstractStateMachine<S extends Enum<S>> implements StateMachine<S> {
    
    /**
     * 验证状态转换是否有效
     * 
     * @param targetState 目标状态
     * @return 是否可以转换到目标状态
     */
    public boolean canTransitionTo(S targetState) {
        S currentState = getCurrentState();
        if (currentState == null || targetState == null) {
            return false;
        }
        
        Set<S> validTransitions = getValidTransitions(currentState);
        return validTransitions.contains(targetState);
    }
    
    /**
     * 安全地转换状态
     *
     * @param targetState 目标状态
     * @param errorMessage 错误信息（可选，用于失败状态）
     * @throws IllegalStateException 如果状态转换无效
     */
    public final void transitionTo(S targetState, String errorMessage) {
        if (!canTransitionTo(targetState)) {
            throw new IllegalStateException(
                String.format("Invalid state transition from %s to %s for entity %s", 
                    getCurrentState(), targetState, getEntityId()));
        }
        
        setCurrentState(targetState);
        // lastUpdated 由数据库自动更新
        
        // 处理错误信息
        if (targetState == getFailedState() && errorMessage != null) {
            setErrorMessage(errorMessage);
        } else if (targetState != getFailedState()) {
            // 非失败状态时清除错误信息
            setErrorMessage(null);
        }
    }
    
    /**
     * 转换到失败状态
     * 
     * @param errorMessage 失败原因
     */
    public void markAsFailed(String errorMessage) {
        transitionTo(getFailedState(), errorMessage);
    }
    
    /**
     * 检查是否为终态
     * 
     * @return 是否为终态
     */
    public boolean isTerminalState() {
        return getTerminalStates().contains(getCurrentState());
    }
    
    /**
     * 检查是否可以重试（从失败状态恢复）
     * 
     * @return 是否可以重试
     */
    public boolean canRetry() {
        return getCurrentState() == getFailedState();
    }
    
    /**
     * 获取状态转换历史描述（用于日志）
     * 
     * @param fromState 源状态
     * @param toState 目标状态
     * @return 转换描述
     */
    protected String getTransitionDescription(S fromState, S toState) {
        return String.format("Entity %s: %s → %s", getEntityId(), fromState, toState);
    }
}
