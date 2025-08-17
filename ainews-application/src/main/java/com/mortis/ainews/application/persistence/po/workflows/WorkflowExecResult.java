package com.mortis.ainews.application.persistence.po.workflows;

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
 * Persistence entity for workflow execution results
 * Maps to the ai_news_workflow_exec_result table
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_news_workflow_exec_result")
public class WorkflowExecResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "result")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> result;

    @Size(max = 100)
    @Column(name = "status", length = 100)
    private String status;

    @NotNull
    @Column(name = "meta", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> meta;

    @Size(max = 100)
    @Column(name = "activity_type", length = 100)
    private String activityType;
}
