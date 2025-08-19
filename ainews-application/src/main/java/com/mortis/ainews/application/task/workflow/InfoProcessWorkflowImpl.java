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

        // 添加日志检查关键词列表
        var keywordList = data.getKeywordDOList();
        Workflow
            .getLogger(InfoProcessWorkflowImpl.class)
            .info(
                "Found {} keywords to process",
                keywordList.size()
            );

        if (keywordList.isEmpty()) {
            Workflow
                .getLogger(InfoProcessWorkflowImpl.class)
                .warn("No keywords found, skipping content fetch");
            return;
        }

        var contentPromises = keywordList
            .stream()
            .map(keywordDO -> {
                Workflow
                    .getLogger(InfoProcessWorkflowImpl.class)
                    .info(
                        "Starting fetchContent for keyword: {}",
                        keywordDO.getContent()
                    );
                return Async.function(() -> activities.fetchContent(keywordDO));
            })
            .toList();

        Workflow
            .getLogger(InfoProcessWorkflowImpl.class)
            .info(
                "Created {} content promises, waiting for completion",
                contentPromises.size()
            );

        Promise
            .allOf(contentPromises)
            .get();

        Workflow
            .getLogger(InfoProcessWorkflowImpl.class)
            .info("All content fetch activities completed");

        var contents = contentPromises
            .stream()
            .map(Promise::get)
            .toList();


        var processedData = activities.process(
            contents,
            data
        );

        activities.save(
            processedData,
            contents,
            data
        );

        activities.notifyUser(processedData);

    }
}
