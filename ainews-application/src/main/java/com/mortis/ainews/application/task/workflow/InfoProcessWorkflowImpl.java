package com.mortis.ainews.application.task.workflow;

import com.mortis.ainews.domain.activities.IInfoProcessActivities;
import com.mortis.ainews.domain.model.InfoProcessWorkflowParams;
import com.mortis.ainews.domain.workflow.IInfoProcessWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class InfoProcessWorkflowImpl implements IInfoProcessWorkflow {
    private final IInfoProcessActivities activities = Workflow.newActivityStub(
        IInfoProcessActivities.class,
        ActivityOptions
            .newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(10))
            .setRetryOptions(RetryOptions
                .newBuilder()
                .setMaximumAttempts(3)
                .build())
            .build()
    );

    @Override
    public void execute(InfoProcessWorkflowParams params) {
        var data = activities.fetchMetadata(
            params.getUserId(),
            params.getScheduleId()
        );
        var contentPromises = data
            .getKeywordDOList()
            .stream()
            .map(keywordDO -> Async.function(() -> activities.fetchContent(keywordDO.getContent())))
            .toList();

        var contents = Promise.allOf(contentPromises);


        //        var processedData = activities.process(
        //            contents,
        //            data
        //        );
        //        var savedData = activities.save(
        //            processedData,
        //            contents.get()
        //                .stream()
        //                .map(content -> content.getId())
        //                .collect(Collectors.toList()),
        //            data
        //        ););

    }
}
