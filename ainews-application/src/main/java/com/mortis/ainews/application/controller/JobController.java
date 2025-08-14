package com.mortis.ainews.application.controller;

import com.mortis.ainews.application.enums.TemporalNamespaceEnum;
import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import com.mortis.ainews.domain.workflow.IHelloWorldWorkflow;  // 导入接口
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mortis.ainews.application.service.facility.ZhipuAIService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class JobController {

    private final ZhipuAIService zhipuAiService;
    private final WorkflowClient workflowClient;

    @Data
    public static class TaskRequest {
        private TemporalTaskTypeEnum workflowType;
        private String input;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody TaskRequest req) {

        String workflowId = "task-" + req.getWorkflowType()
                .getValue()
                .toLowerCase() + "-" + UUID.randomUUID();
        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalNamespaceEnum.Job.getValue())
                .setWorkflowId(workflowId)
                .build();

        // 使用接口而不是实现类
        var workflowStub = workflowClient.newWorkflowStub(
                IHelloWorldWorkflow.class,
                // 正确使用接口
                workflowOptions
        );

        WorkflowClient.start(
                workflowStub::sayHello,
                req.getInput()
        );

        return new ResponseEntity<>(
                workflowId,
                HttpStatus.OK
        );
    }
}
