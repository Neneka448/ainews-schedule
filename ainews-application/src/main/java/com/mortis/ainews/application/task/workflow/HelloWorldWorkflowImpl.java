package com.mortis.ainews.application.task.workflow;


import com.mortis.ainews.interfaces.activities.IHelloWorldActivity;
import com.mortis.ainews.interfaces.workflow.IHelloWorldWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class HelloWorldWorkflowImpl implements IHelloWorldWorkflow {
    private final IHelloWorldActivity activities = Workflow.newActivityStub(
            IHelloWorldActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .build()
    );

    @Override
    public String sayHello(String name) {
        return activities.say(name);
    }
}
