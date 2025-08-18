package com.mortis.ainews.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Log type enumeration for categorizing different types of logs
 * This enum is designed to be extensible for future log types
 */
@Getter
public enum LogTypeEnum {
    /**
     * Common log type for general application logs
     * belong_id: -1 (no specific entity association)
     * extra: empty but extensible for future use
     */
    COMMON("COMMON", "General application logs");

    private final String value;
    private final String description;

    LogTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LogTypeEnum fromValue(String value) {
        for (LogTypeEnum type : LogTypeEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown log type value: " + value);
    }

    /**
     * Get the default belong_id for this log type
     * @return default belong_id value
     */
    public Long getDefaultBelongId() {
        switch (this) {
            case COMMON:
                return -1L; // No specific entity association
            default:
                return -1L;
        }
    }

    /**
     * Check if this log type supports custom belong_id
     * @return true if custom belong_id is supported
     */
    public boolean supportsCustomBelongId() {
        switch (this) {
            case COMMON:
                return false; // COMMON type always uses -1
            default:
                return true; // Future types may support custom belong_id
        }
    }
}
