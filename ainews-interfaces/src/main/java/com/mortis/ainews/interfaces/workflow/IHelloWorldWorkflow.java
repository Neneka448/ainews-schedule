package com.mortis.ainews.interfaces.workflow;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IHelloWorldWorkflow {
    @WorkflowMethod
    String sayHello(String name);
}
