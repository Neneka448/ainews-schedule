package com.mortis.ainews.application.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 正常
    SUCCESS(
            0,
            "成功"
    ),

    // 通用错误
    COMMON_ERROR(
            10001,
            "未知错误"
    ),
    PARAMS_VALIDATION_ERROR(
            10002,
            "参数校验错误"
    ),

    // UserService错误
    USER_NOT_FOUND(
            20001,
            "用户不存在"
    ),
    USER_ALREADY_EXISTS(
            20002,
            "用户已存在"
    ),

    // KeywordService错误
    KEYWORD_NOT_FOUND(
            30001,
            "关键字不存在"
    ),
    KEYWORDS_LIST_EMPTY(
            30002,
            "关键字列表为空或关键词均不存在"
    ),

    // ScheduleService错误
    WORKFLOW_TYPE_NOT_SUPPORTED(
            40001,
            "不支持的工作流类型"
    ),
    SCHEDULE_SPEC_INVALID(
            40002,
            "计划规格无效"
    ),
    TAG_IDS_NOT_FOUND(
            40003,
            "部分或全部标签ID不存在"
    );

    private final int code;
    private final String message;

}
