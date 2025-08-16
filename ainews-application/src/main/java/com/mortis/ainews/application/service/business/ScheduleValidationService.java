package com.mortis.ainews.application.service.business;

import com.mortis.ainews.application.dto.ScheduleCreateRequest;
import com.mortis.ainews.application.dto.ScheduleSpecDTO;
import com.mortis.ainews.application.helper.ErrorCode;
import com.mortis.ainews.application.helper.ParamsValidationException;
import com.mortis.ainews.application.helper.TemporalWorkflowMappingHelper;
import com.mortis.ainews.application.persistence.repository.interfaces.KeywordRepository;
import com.mortis.ainews.application.persistence.repository.interfaces.UserRepository;
import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleValidationService {

    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final TemporalWorkflowMappingHelper temporalWorkflowMappingHelper;

    /**
     * 验证ScheduleCreateRequest的所有字段
     */
    public void validateScheduleCreateRequest(ScheduleCreateRequest request) {
        validateWorkflowType(request.getWorkflowType());
        validateUserId(request.getUserId());
        validateSpec(request.getSpec());
        validateTagIds(request.getTagIds());
    }

    /**
     * 验证工作流类型是否存在且受支持
     */
    public void validateWorkflowType(TemporalTaskTypeEnum workflowType) {
        if (workflowType == null) {
            throw new ParamsValidationException("工作流类型不能为空");
        }

        // 检查工作流类型是否在映射中存在
        if (temporalWorkflowMappingHelper
            .getWorkflowClassByType(workflowType)
            .isEmpty())
        {
            log.warn(
                "Unsupported workflow type: {}",
                workflowType
            );
            throw new ParamsValidationException("不支持的工作流类型: " + workflowType.getValue());
        }
    }

    /**
     * 验证用户ID是否存在
     */
    public void validateUserId(Long userId) {
        if (userId == null) {
            throw new ParamsValidationException("用户ID不能为空");
        }

        if (userId <= 0) {
            throw new ParamsValidationException("用户ID必须为正数");
        }

        // 检查用户是否存在且未删除
        if (userRepository
            .findByIdAndDeleted(
                userId,
                0
            )
            .isEmpty())
        {
            log.warn(
                "User not found or deleted: {}",
                userId
            );
            throw new ParamsValidationException("用户不存在或已删除: " + userId);
        }
    }

    /**
     * 验证计划规格的有效性
     * 参考TemporalScheduleService中的parseSpecItem方法的验证逻辑
     */
    public void validateSpec(ScheduleSpecDTO spec) {
        if (spec == null) {
            throw new ParamsValidationException("Schedule日程不能为空");
        }

        try {
            // 验证各个时间字段
            validateSpecField(
                "second",
                spec.getSecond(),
                0,
                59
            );
            validateSpecField(
                "minute",
                spec.getMinute(),
                0,
                59
            );
            validateSpecField(
                "hour",
                spec.getHour(),
                0,
                23
            );
            validateSpecField(
                "dayOfMonth",
                spec.getDayOfMonth(),
                1,
                31
            );
            validateSpecField(
                "month",
                spec.getMonth(),
                1,
                12
            );
            validateSpecField(
                "year",
                spec.getYear(),
                1970,
                9999
            );
            validateSpecField(
                "dayOfWeek",
                spec.getDayOfWeek(),
                0,
                6
            );
        } catch (IllegalArgumentException e) {
            log.warn(
                "Invalid schedule spec: {}",
                e.getMessage()
            );
            throw new ParamsValidationException("计划规格无效: " + e.getMessage());
        }
    }

    /**
     * 验证标签ID列表是否存在
     */
    public void validateTagIds(List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            // 标签ID列表可以为空，这是允许的
            return;
        }

        // 检查是否有无效的ID（null或非正数）
        for (Long tagId : tagIds) {
            if (tagId == null || tagId <= 0) {
                throw new ParamsValidationException("标签ID必须为正数");
            }
        }

        // 检查所有标签ID是否存在且未删除
        List<Long> existingTagIds = keywordRepository
            .findByIdInAndDeleted(
                tagIds,
                0
            )
            .stream()
            .map(keyword -> keyword.getId())
            .toList();

        if (existingTagIds.size() != tagIds.size()) {
            List<Long> missingTagIds = tagIds
                .stream()
                .filter(id -> !existingTagIds.contains(id))
                .toList();
            log.warn(
                "Tag IDs not found or deleted: {}",
                missingTagIds
            );
            throw new ParamsValidationException("部分标签ID不存在或已删除: " + missingTagIds);
        }
    }

    /**
     * 验证单个规格字段
     * 基于TemporalScheduleService.parseSpecItem的验证逻辑
     */
    private void validateSpecField(String fieldName, String value, int minValue, int maxValue) {
        if (!StringUtils.hasText(value)) {
            return; // 空值是允许的，会使用默认值
        }

        // 通配符 "*" 总是有效的
        if ("*".equals(value)) {
            return;
        }

        // 验证逗号分隔的数字列表: "1,2,3" 或 "5"
        if (value.matches("^\\d+(,\\d+)*$")) {
            String[] parts = value.split(",");
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < minValue || num > maxValue) {
                    throw new IllegalArgumentException(
                        String.format(
                            "%s值 %d 超出范围 [%d-%d]",
                            fieldName,
                            num,
                            minValue,
                            maxValue
                        ));
                }
            }
            return;
        }

        // 验证范围格式: "1-5"
        if (value.matches("^\\d+-\\d+$")) {
            String[] parts = value.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException(fieldName + "范围格式无效: " + value);
            }
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start < minValue || end > maxValue || start > end) {
                throw new IllegalArgumentException(
                    String.format(
                        "%s范围 %s 无效，应在 [%d-%d] 范围内且起始值不大于结束值",
                        fieldName,
                        value,
                        minValue,
                        maxValue
                    ));
            }
            return;
        }

        // 验证步长格式: "0/5"
        if (value.matches("^\\d+/\\d+$")) {
            String[] parts = value.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException(fieldName + "步长格式无效: " + value);
            }
            int start = Integer.parseInt(parts[0]);
            int step = Integer.parseInt(parts[1]);
            if (start < minValue || start > maxValue || step <= 0) {
                throw new IllegalArgumentException(
                    String.format(
                        "%s步长 %s 无效，起始值应在 [%d-%d] 范围内，步长应大于0",
                        fieldName,
                        value,
                        minValue,
                        maxValue
                    ));
            }
            return;
        }

        // 如果都不匹配，则格式无效
        throw new IllegalArgumentException(fieldName + "格式无效: " + value +
            "，支持的格式: *, 数字, 数字列表(1,2,3), 范围(1-5), 步长(0/5)");
    }
}
