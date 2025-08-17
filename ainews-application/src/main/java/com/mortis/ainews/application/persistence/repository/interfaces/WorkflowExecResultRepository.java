package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.workflows.WorkflowExecResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for workflow execution results
 * Provides data access operations for WorkflowExecResult entities
 */
@Repository
public interface WorkflowExecResultRepository extends JpaRepository<WorkflowExecResult, Long> {

    /**
     * Find all workflow execution results by schedule ID
     * 
     * @param scheduleId the schedule ID to search for
     * @return list of workflow execution results for the given schedule
     */
    List<WorkflowExecResult> findByScheduleId(Long scheduleId);

    /**
     * Find all workflow execution results by schedule ID and status
     * 
     * @param scheduleId the schedule ID to search for
     * @param status the status to filter by
     * @return list of workflow execution results matching the criteria
     */
    List<WorkflowExecResult> findByScheduleIdAndStatus(Long scheduleId, String status);

    /**
     * Find all workflow execution results by activity type
     * 
     * @param activityType the activity type to search for
     * @return list of workflow execution results for the given activity type
     */
    List<WorkflowExecResult> findByActivityType(String activityType);

    /**
     * Find all workflow execution results by status
     * 
     * @param status the status to search for
     * @return list of workflow execution results with the given status
     */
    List<WorkflowExecResult> findByStatus(String status);

    /**
     * Find workflow execution results created after a specific timestamp
     * 
     * @param createdAt the timestamp to search after
     * @return list of workflow execution results created after the given timestamp
     */
    List<WorkflowExecResult> findByCreatedAtAfter(Instant createdAt);

    /**
     * Find the most recent workflow execution result for a schedule
     * 
     * @param scheduleId the schedule ID to search for
     * @return the most recent workflow execution result for the schedule, if any
     */
    Optional<WorkflowExecResult> findFirstByScheduleIdOrderByCreatedAtDesc(Long scheduleId);

    /**
     * Find workflow execution results by schedule ID and activity type
     * 
     * @param scheduleId the schedule ID to search for
     * @param activityType the activity type to filter by
     * @return list of workflow execution results matching the criteria
     */
    List<WorkflowExecResult> findByScheduleIdAndActivityType(Long scheduleId, String activityType);
}
