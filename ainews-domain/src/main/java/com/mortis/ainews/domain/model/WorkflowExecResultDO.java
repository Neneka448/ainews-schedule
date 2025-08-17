package com.mortis.ainews.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain object for workflow execution results
 * Represents the result of a workflow execution with metadata and status tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class WorkflowExecResultDO {
    /**
     * Unique identifier for the workflow execution result
     */
    private Long id;

    /**
     * Reference to the schedule that triggered this workflow execution
     */
    private Long scheduleId;

    /**
     * Timestamp when the workflow execution result was created
     */
    private Instant createdAt;

    /**
     * JSON result data from the workflow execution
     * Can contain any structured data returned by the workflow
     */
    private Map<String, Object> result;

    /**
     * Current status of the workflow execution
     * Examples: "RUNNING", "COMPLETED", "FAILED", "CANCELLED"
     */
    private String status;

    /**
     * Metadata associated with the workflow execution
     * Contains additional context and configuration data
     */
    private Map<String, Object> meta;

    /**
     * Type of activity that was executed
     * Examples: "SEND_EMAIL", "PROCESS_DATA", "GENERATE_REPORT"
     */
    private String activityType;
}
