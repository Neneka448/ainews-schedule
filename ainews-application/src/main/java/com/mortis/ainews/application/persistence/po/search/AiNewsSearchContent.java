package com.mortis.ainews.application.persistence.po.search;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_news_search_content")
public class AiNewsSearchContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @NotNull
    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "meta")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> meta;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
