package com.mortis.ainews.domain.workflow;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IInfoProcessWorkflow {
    

    @WorkflowMethod
    void process();
}
