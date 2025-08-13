package com.mortis.ainews.application.enums;


import lombok.Getter;

@Getter
public enum TemporalNamespaceEnum {
    Job("job"), // 短时作业
    Pipeline("pipeline"); // 长周期流水线

    private final String value;

    TemporalNamespaceEnum(String value) {
        this.value = value;
    }
}
