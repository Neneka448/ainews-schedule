package com.mortis.ainews.application.persistence.converter;

import com.mortis.ainews.application.persistence.po.workflows.WorkflowExecResult;
import com.mortis.ainews.domain.model.WorkflowExecResultDO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct converter for WorkflowExecResult entities
 * Handles conversion between domain objects (DO) and persistence objects (PO)
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkflowExecResultConverter {

    /**
     * Convert persistence object to domain object
     * 
     * @param workflowExecResult the persistence object to convert
     * @return the corresponding domain object
     */
    WorkflowExecResultDO toDO(WorkflowExecResult workflowExecResult);

    /**
     * Convert list of persistence objects to list of domain objects
     * 
     * @param workflowExecResults the list of persistence objects to convert
     * @return the corresponding list of domain objects
     */
    List<WorkflowExecResultDO> toDOs(List<WorkflowExecResult> workflowExecResults);

    /**
     * Convert domain object to persistence object
     * 
     * @param workflowExecResultDO the domain object to convert
     * @return the corresponding persistence object
     */
    WorkflowExecResult toPO(WorkflowExecResultDO workflowExecResultDO);

    /**
     * Convert list of domain objects to list of persistence objects
     * 
     * @param workflowExecResultDOs the list of domain objects to convert
     * @return the corresponding list of persistence objects
     */
    List<WorkflowExecResult> toPOs(List<WorkflowExecResultDO> workflowExecResultDOs);
}
