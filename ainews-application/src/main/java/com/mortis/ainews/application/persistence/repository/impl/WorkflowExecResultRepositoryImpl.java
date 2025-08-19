package com.mortis.ainews.application.persistence.repository.impl;

import com.mortis.ainews.application.persistence.converter.facade.ConverterFacade;
import com.mortis.ainews.application.persistence.po.workflows.WorkflowExecResult;
import com.mortis.ainews.application.persistence.repository.interfaces.WorkflowExecResultRepository;
import com.mortis.ainews.domain.model.WorkflowExecResultDO;
import com.mortis.ainews.domain.model.PageQuery;
import com.mortis.ainews.domain.model.PageData;
import com.mortis.ainews.domain.repository.IWorkflowExecResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the domain repository interface for workflow execution results
 * Bridges the domain layer with the persistence layer using Spring Data JPA
 * <p>
 * 注意：类名避免以 RepositoryImpl 结尾，防止被 Spring Data 误认为是自定义实现导致循环依赖。
 */
@Repository
@RequiredArgsConstructor
@Slf4j
class WorkflowExecResultDomainRepository implements IWorkflowExecResultRepository {

    private final WorkflowExecResultRepository workflowExecResultRepository;
    private final ConverterFacade converterFacade;

    @Override
    @Transactional
    public WorkflowExecResultDO save(WorkflowExecResultDO workflowExecResult) {
        log.debug(
            "Saving workflow execution result for schedule ID: {}",
            workflowExecResult.getScheduleId()
        );

        WorkflowExecResult po = converterFacade.workflowExecResultConverter.toPO(workflowExecResult);
        WorkflowExecResult savedPo = workflowExecResultRepository.save(po);

        return converterFacade.workflowExecResultConverter.toDO(savedPo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowExecResultDO> findById(Long id) {
        log.debug(
            "Finding workflow execution result by ID: {}",
            id
        );

        return workflowExecResultRepository
            .findById(id)
            .map(converterFacade.workflowExecResultConverter::toDO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByScheduleId(Long scheduleId) {
        log.debug(
            "Finding workflow execution results for schedule ID: {}",
            scheduleId
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByScheduleId(scheduleId);
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional(readOnly = true)
    public PageData<WorkflowExecResultDO> findByScheduleIdWithPaging(Long scheduleId, PageQuery pageQuery) {
        log.debug(
            "Finding workflow execution results for schedule ID: {} with pagination",
            scheduleId
        );

        Pageable pageable = PageRequest.of(
            pageQuery.getPageNum() - 1,
            // Spring Data starts from 0
            pageQuery.getPageSize(),
            Sort.by(
                Sort.Direction.DESC,
                "createdAt"
            )
            // Most recent first
        );

        // Note: Spring Data JPA doesn't have built-in pagination for custom queries like findByScheduleId
        // For now, we'll use the basic findAll with manual filtering
        // In a production environment, you might want to add a custom query method
        Page<WorkflowExecResult> page = workflowExecResultRepository.findAll(pageable);

        // Filter by scheduleId manually (this is not optimal for large datasets)
        List<WorkflowExecResult> filteredResults = page
            .getContent()
            .stream()
            .filter(result -> result
                .getScheduleId()
                .equals(scheduleId))
            .toList();

        List<WorkflowExecResultDO> workflowExecResultDOs = converterFacade.workflowExecResultConverter.toDOs(filteredResults);

        return new PageData<>(
            workflowExecResultDOs,
            (long) filteredResults.size(),
            // This is not accurate for total count
            pageQuery
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByStatus(String status) {
        log.debug(
            "Finding workflow execution results by status: {}",
            status
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByStatus(status);
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByActivityType(String activityType) {
        log.debug(
            "Finding workflow execution results by activity type: {}",
            activityType
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByActivityType(activityType);
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowExecResultDO> findLatestByScheduleId(Long scheduleId) {
        log.debug(
            "Finding latest workflow execution result for schedule ID: {}",
            scheduleId
        );

        return workflowExecResultRepository
            .findFirstByScheduleIdOrderByCreatedAtDesc(scheduleId)
            .map(converterFacade.workflowExecResultConverter::toDO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByCreatedAtAfter(Instant createdAt) {
        log.debug(
            "Finding workflow execution results created after: {}",
            createdAt
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByCreatedAtAfter(createdAt);
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByScheduleIdAndStatus(Long scheduleId, String status) {
        log.debug(
            "Finding workflow execution results for schedule ID: {} and status: {}",
            scheduleId,
            status
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByScheduleIdAndStatus(
            scheduleId,
            status
        );
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecResultDO> findByScheduleIdAndActivityType(Long scheduleId, String activityType) {
        log.debug(
            "Finding workflow execution results for schedule ID: {} and activity type: {}",
            scheduleId,
            activityType
        );

        List<WorkflowExecResult> pos = workflowExecResultRepository.findByScheduleIdAndActivityType(
            scheduleId,
            activityType
        );
        return converterFacade.workflowExecResultConverter.toDOs(pos);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug(
            "Deleting workflow execution result by ID: {}",
            id
        );

        workflowExecResultRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByScheduleId(Long scheduleId) {
        log.debug(
            "Counting workflow execution results for schedule ID: {}",
            scheduleId
        );

        return workflowExecResultRepository
            .findByScheduleId(scheduleId)
            .size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        log.debug(
            "Counting workflow execution results by status: {}",
            status
        );

        return workflowExecResultRepository
            .findByStatus(status)
            .size();
    }
}
