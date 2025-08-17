package com.mortis.ainews.application.service.business;

import com.mortis.ainews.application.persistence.converter.facade.ConverterFacade;
import com.mortis.ainews.application.persistence.repository.interfaces.ScheduleRepository;
import com.mortis.ainews.domain.enums.ScheduleStatusEnum;
import com.mortis.ainews.domain.model.ScheduleDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Schedule 状态管理服务
 * 负责协调 Domain 状态转换和数据库持久化
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleStateService {

    private final ScheduleRepository scheduleRepository;
    private final ConverterFacade converterFacade;

    /**
     * 安全地转换 Schedule 状态并持久化到数据库
     * 
     * @param scheduleId 计划ID
     * @param targetStatus 目标状态
     * @param errorMessage 错误信息（可选）
     * @return 更新后的 ScheduleDO
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO transitionScheduleStatus(Long scheduleId, ScheduleStatusEnum targetStatus, String errorMessage) {
        // 1. 从数据库获取当前 Schedule
        var scheduleEntity = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
        
        var scheduleDO = converterFacade.scheduleConverter.toDO(scheduleEntity);
        
        // 2. 使用 Domain 层的状态机进行状态转换（包含业务规则验证）
        scheduleDO.transitionTo(targetStatus, errorMessage);
        
        // 3. 持久化到数据库
        var updatedEntity = scheduleRepository.save(converterFacade.scheduleConverter.toPO(scheduleDO));
        
        // 4. 记录状态转换日志
        log.info("Schedule {} status changed to {} at {}", 
            scheduleId, targetStatus, scheduleDO.getLastUpdated());
        
        if (errorMessage != null) {
            log.warn("Schedule {} failed with error: {}", scheduleId, errorMessage);
        }
        
        return converterFacade.scheduleConverter.toDO(updatedEntity);
    }

    /**
     * 将 Schedule 标记为运行中
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO markScheduleAsRunning(Long scheduleId) {
        return transitionScheduleStatus(scheduleId, ScheduleStatusEnum.RUNNING, null);
    }

    /**
     * 将 Schedule 标记为失败
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO markScheduleAsFailed(Long scheduleId, String errorMessage) {
        return transitionScheduleStatus(scheduleId, ScheduleStatusEnum.FAILED, errorMessage);
    }

    /**
     * 将 Schedule 标记为完成
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO markScheduleAsCompleted(Long scheduleId) {
        return transitionScheduleStatus(scheduleId, ScheduleStatusEnum.COMPLETED, null);
    }

    /**
     * 将 Schedule 标记为取消
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO markScheduleAsCancelled(Long scheduleId) {
        return transitionScheduleStatus(scheduleId, ScheduleStatusEnum.CANCELLED, null);
    }

    /**
     * 重试失败的 Schedule
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO retryFailedSchedule(Long scheduleId) {
        // 获取当前状态
        var scheduleEntity = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
        
        var scheduleDO = converterFacade.scheduleConverter.toDO(scheduleEntity);
        
        // 验证是否可以重试
        if (!scheduleDO.canRetry()) {
            throw new IllegalStateException(
                String.format("Schedule %d cannot be retried, current status: %s", 
                    scheduleId, scheduleDO.getCurrentState()));
        }
        
        // 重试（转换到 RUNNING 状态）
        return markScheduleAsRunning(scheduleId);
    }

    /**
     * 批量更新状态（用于批处理场景）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateStatus(java.util.List<Long> scheduleIds, ScheduleStatusEnum targetStatus, String errorMessage) {
        for (Long scheduleId : scheduleIds) {
            try {
                transitionScheduleStatus(scheduleId, targetStatus, errorMessage);
            } catch (Exception e) {
                log.error("Failed to update status for schedule {}: {}", scheduleId, e.getMessage());
                // 继续处理其他 schedule，不中断批处理
            }
        }
    }

    /**
     * 检查 Schedule 是否可以转换到指定状态
     */
    public boolean canTransitionTo(Long scheduleId, ScheduleStatusEnum targetStatus) {
        var scheduleEntity = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
        
        var scheduleDO = converterFacade.scheduleConverter.toDO(scheduleEntity);
        return scheduleDO.canTransitionTo(targetStatus);
    }
}
