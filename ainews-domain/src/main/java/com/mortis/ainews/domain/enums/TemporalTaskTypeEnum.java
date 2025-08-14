package com.mortis.ainews.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TemporalTaskTypeEnum {
    SEND_USER_NEWS_BY_EMAIL("send_user_news_by_email");

    private final String value;

    TemporalTaskTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TemporalTaskTypeEnum fromValue(String value) {
        for (TemporalTaskTypeEnum type : TemporalTaskTypeEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
