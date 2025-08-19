package com.mortis.ainews.application.persistence.po.logs;

import com.mortis.ainews.domain.enums.LogTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

/**
 * JPA Entity for ai_news_logs table
 * Stores application logs with SLF4J integration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_news_logs")
public class AiNewsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Log level compatible with SLF4J levels
     * Values: TRACE, DEBUG, INFO, WARN, ERROR
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "level", nullable = false, length = 100)
    private String level;

    /**
     * Timestamp when the log was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Log type categorization
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 100)
    private LogTypeEnum type;

    /**
     * Log message content
     */
    @Lob
    @Column(name = "message", columnDefinition = "LONGTEXT")
    private String message;

    /**
     * Additional log data in JSON format
     * Content varies based on log type
     */
    @Column(name = "extra")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> extra;

    /**
     * Entity ID that this log belongs to
     * Interpretation depends on log type:
     * - COMMON: -1 (no specific entity)
     * - Future types: specific entity IDs
     */
    @NotNull
    @Column(name = "belong_id", nullable = false)
    private Long belongId;

    /**
     * Constructor for creating logs with basic information
     */
    public AiNewsLog(String level, LogTypeEnum type, String message, Long belongId) {
        this.level = level;
        this.type = type;
        this.message = message;
        this.belongId = belongId;
    }

    /**
     * Constructor for creating logs with extra data
     */
    public AiNewsLog(String level, LogTypeEnum type, String message, Long belongId, Map<String, Object> extra) {
        this.level = level;
        this.type = type;
        this.message = message;
        this.belongId = belongId;
        this.extra = extra;
    }
}
