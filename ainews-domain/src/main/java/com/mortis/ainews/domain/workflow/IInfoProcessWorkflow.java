package com.mortis.ainews.domain.workflow;


import com.mortis.ainews.domain.model.InfoProcessWorkflowParams;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IInfoProcessWorkflow {


    @WorkflowMethod
    void execute(InfoProcessWorkflowParams params);
}
