package com.mortis.ainews.application.config;


import io.temporal.client.WorkflowClient;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TemporalServiceConfig {

    private final WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

    @Bean
    public WorkflowClient temporalClient() {
        return WorkflowClient.newInstance(serviceStub);
    }

    @Bean
    public ScheduleClient scheduleClient() {
        return ScheduleClient.newInstance(serviceStub);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient temporalClient) {
        log.info("Creating Temporal WorkerFactory...");
        return WorkerFactory.newInstance(temporalClient);
    }
}
