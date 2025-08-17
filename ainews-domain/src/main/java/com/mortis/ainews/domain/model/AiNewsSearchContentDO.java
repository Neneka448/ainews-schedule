package com.mortis.ainews.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class AiNewsSearchContentDO {
    private Long id;
    private Long scheduleId;
    private Long keywordId;
    private String content;
    private Map<String, Object> meta;
    private Instant createdAt;
}
