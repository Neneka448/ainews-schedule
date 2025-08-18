package com.mortis.ainews.application.service.log;

import com.mortis.ainews.domain.enums.LogTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for managing log type mappings and extra data parsing
 * Provides extensible design for different log types with specific
 * interpretations of belong_id and extra field content
 */
@Component
@Slf4j
public class LogHelper {

    /**
     * Get the appropriate belong_id for a given log type and context
     * @param logType the log type
     * @param contextBelongId optional context-specific belong_id
     * @return the belong_id to use for this log entry
     */
    public Long resolveBelongId(LogTypeEnum logType, Long contextBelongId) {
        switch (logType) {
            case COMMON:
                // COMMON type always uses -1, ignoring any context
                return logType.getDefaultBelongId();
            default:
                // Future log types may use context-specific belong_id
                return contextBelongId != null ? contextBelongId : logType.getDefaultBelongId();
        }
    }

    /**
     * Parse and validate extra data for a specific log type
     * @param logType the log type
     * @param rawExtra raw extra data map
     * @return processed extra data map
     */
    public Map<String, Object> parseExtraData(LogTypeEnum logType, Map<String, Object> rawExtra) {
        if (rawExtra == null) {
            rawExtra = new HashMap<>();
        }

        switch (logType) {
            case COMMON:
                return parseCommonExtraData(rawExtra);
            default:
                log.warn("Unknown log type for extra data parsing: {}", logType);
                return rawExtra;
        }
    }

    /**
     * Parse extra data for COMMON log type
     * Currently returns empty map but designed for future extensibility
     * @param rawExtra raw extra data
     * @return processed extra data for COMMON type
     */
    private Map<String, Object> parseCommonExtraData(Map<String, Object> rawExtra) {
        Map<String, Object> processedExtra = new HashMap<>();
        
        // For COMMON type, we currently don't store extra data
        // but this method provides a place for future enhancements
        // such as adding standard fields like:
        // - thread_name
        // - class_name
        // - method_name
        // - trace_id
        // - span_id
        
        // Example future implementation:
        // if (rawExtra.containsKey("threadName")) {
        //     processedExtra.put("thread_name", rawExtra.get("threadName"));
        // }
        
        return processedExtra;
    }

    /**
     * Create a log context for a specific entity
     * This method will be extended when new log types are added
     * @param logType the log type
     * @param entityId the entity ID
     * @param additionalContext additional context data
     * @return log context map
     */
    public LogContext createLogContext(LogTypeEnum logType, Long entityId, Map<String, Object> additionalContext) {
        Long belongId = resolveBelongId(logType, entityId);
        Map<String, Object> extraData = parseExtraData(logType, additionalContext);
        
        return new LogContext(logType, belongId, extraData);
    }

    /**
     * Create a simple log context for COMMON type logs
     * @return log context for COMMON type
     */
    public LogContext createCommonLogContext() {
        return createLogContext(LogTypeEnum.COMMON, null, null);
    }

    /**
     * Validate if a log type supports the given belong_id
     * @param logType the log type
     * @param belongId the belong_id to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidBelongId(LogTypeEnum logType, Long belongId) {
        switch (logType) {
            case COMMON:
                // COMMON type only accepts -1
                return belongId != null && belongId.equals(-1L);
            default:
                // Future types may have different validation rules
                return belongId != null;
        }
    }

    /**
     * Get description of what belong_id represents for a given log type
     * @param logType the log type
     * @return description of belong_id meaning
     */
    public String getBelongIdDescription(LogTypeEnum logType) {
        switch (logType) {
            case COMMON:
                return "No specific entity association (always -1)";
            default:
                return "Entity-specific ID (interpretation depends on log type)";
        }
    }

    /**
     * Inner class representing log context
     */
    public static class LogContext {
        private final LogTypeEnum logType;
        private final Long belongId;
        private final Map<String, Object> extraData;

        public LogContext(LogTypeEnum logType, Long belongId, Map<String, Object> extraData) {
            this.logType = logType;
            this.belongId = belongId;
            this.extraData = extraData != null ? extraData : new HashMap<>();
        }

        public LogTypeEnum getLogType() {
            return logType;
        }

        public Long getBelongId() {
            return belongId;
        }

        public Map<String, Object> getExtraData() {
            return extraData;
        }
    }
}
