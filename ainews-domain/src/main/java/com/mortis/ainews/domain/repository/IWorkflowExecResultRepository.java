package com.mortis.ainews.domain.repository;

import com.mortis.ainews.domain.model.WorkflowExecResultDO;
import com.mortis.ainews.domain.model.PageQuery;
import com.mortis.ainews.domain.model.PageData;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for workflow execution results
 * Provides business-oriented data access operations for workflow execution tracking
 */
public interface IWorkflowExecResultRepository {

    /**
     * Save a workflow execution result
     * 
     * @param workflowExecResult the workflow execution result to save
     * @return the saved workflow execution result with generated ID
     */
    WorkflowExecResultDO save(WorkflowExecResultDO workflowExecResult);

    /**
     * Find a workflow execution result by ID
     * 
     * @param id the ID to search for
     * @return the workflow execution result if found
     */
    Optional<WorkflowExecResultDO> findById(Long id);

    /**
     * Find all workflow execution results for a specific schedule
     * 
     * @param scheduleId the schedule ID to search for
     * @return list of workflow execution results for the schedule
     */
    List<WorkflowExecResultDO> findByScheduleId(Long scheduleId);

    /**
     * Find workflow execution results for a schedule with pagination
     * 
     * @param scheduleId the schedule ID to search for
     * @param pageQuery pagination parameters
     * @return paginated workflow execution results
     */
    PageData<WorkflowExecResultDO> findByScheduleIdWithPaging(Long scheduleId, PageQuery pageQuery);

    /**
     * Find workflow execution results by status
     * 
     * @param status the status to filter by
     * @return list of workflow execution results with the given status
     */
    List<WorkflowExecResultDO> findByStatus(String status);

    /**
     * Find workflow execution results by activity type
     * 
     * @param activityType the activity type to filter by
     * @return list of workflow execution results for the given activity type
     */
    List<WorkflowExecResultDO> findByActivityType(String activityType);

    /**
     * Find the most recent workflow execution result for a schedule
     * 
     * @param scheduleId the schedule ID to search for
     * @return the most recent workflow execution result for the schedule, if any
     */
    Optional<WorkflowExecResultDO> findLatestByScheduleId(Long scheduleId);

    /**
     * Find workflow execution results created after a specific timestamp
     * 
     * @param createdAt the timestamp to search after
     * @return list of workflow execution results created after the given timestamp
     */
    List<WorkflowExecResultDO> findByCreatedAtAfter(Instant createdAt);

    /**
     * Find workflow execution results by schedule ID and status
     * 
     * @param scheduleId the schedule ID to search for
     * @param status the status to filter by
     * @return list of workflow execution results matching the criteria
     */
    List<WorkflowExecResultDO> findByScheduleIdAndStatus(Long scheduleId, String status);

    /**
     * Find workflow execution results by schedule ID and activity type
     * 
     * @param scheduleId the schedule ID to search for
     * @param activityType the activity type to filter by
     * @return list of workflow execution results matching the criteria
     */
    List<WorkflowExecResultDO> findByScheduleIdAndActivityType(Long scheduleId, String activityType);

    /**
     * Delete a workflow execution result by ID
     * 
     * @param id the ID of the workflow execution result to delete
     */
    void deleteById(Long id);

    /**
     * Count workflow execution results for a specific schedule
     * 
     * @param scheduleId the schedule ID to count for
     * @return the number of workflow execution results for the schedule
     */
    long countByScheduleId(Long scheduleId);

    /**
     * Count workflow execution results by status
     * 
     * @param status the status to count for
     * @return the number of workflow execution results with the given status
     */
    long countByStatus(String status);
}
