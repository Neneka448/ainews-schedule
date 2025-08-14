package com.mortis.ainews.application.config;

import com.mortis.ainews.application.enums.TemporalNamespaceEnum;
import com.mortis.ainews.application.helper.TemporalWorkflowMappingHelper;
import com.mortis.ainews.application.task.workflow.HelloWorldWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TemporalWorkerConfig {
    private static final Logger log = LoggerFactory.getLogger(TemporalWorkerConfig.class);

    private final WorkerFactory workerFactory;
    private final TemporalWorkflowMappingHelper temporalWorkflowMappingHelper;

    public TemporalWorkerConfig(
            WorkerFactory workerFactory, TemporalWorkflowMappingHelper temporalWorkflowMappingHelper)
    {
        this.temporalWorkflowMappingHelper = temporalWorkflowMappingHelper;
        this.workerFactory = workerFactory;
    }


    @PostConstruct
    public void configureAndStartWorkers() {
        log.info("Configuring Temporal Workers...");

        WorkerOptions jobWorkerOptions = WorkerOptions.newBuilder()
                .setMaxConcurrentActivityExecutionSize(4)
                .build();
        Worker jobWorker = workerFactory.newWorker(
                TemporalNamespaceEnum.Job.getValue(),
                jobWorkerOptions
        );
        temporalWorkflowMappingHelper.registerWorkflowToWorker(jobWorker);
        temporalWorkflowMappingHelper.registerActivitiesToWorker(jobWorker);


        // TODO
        WorkerOptions pipelineWorkerOptions = WorkerOptions.newBuilder()
                .setMaxConcurrentActivityExecutionSize(4)
                .build();
        Worker pipelineWorker = workerFactory.newWorker(
                TemporalNamespaceEnum.Pipeline.getValue(),
                pipelineWorkerOptions
        );


        log.info("Starting Temporal WorkerFactory...");
        workerFactory.start();
        log.info("Temporal WorkerFactory started successfully.");
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down Temporal WorkerFactory...");
        workerFactory.shutdown();
        log.info("Temporal WorkerFactory shut down.");
    }
}
