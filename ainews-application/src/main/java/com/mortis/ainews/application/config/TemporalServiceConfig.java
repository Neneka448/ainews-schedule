package com.mortis.ainews.application.config;


import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalServiceConfig {
    private static final Logger log = LoggerFactory.getLogger(TemporalServiceConfig.class);

    private final WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    @Bean
    public WorkflowClient temporalClient() {
        return WorkflowClient.newInstance(serviceStub);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient temporalClient) {
        log.info("Creating Temporal WorkerFactory...");
        return WorkerFactory.newInstance(temporalClient);
    }
}
