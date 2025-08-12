package com.mortis.ainews.application.service.jobs;

import java.time.LocalDateTime;

import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EchoService {
    @Job
    public void echo() {
        log.info("Echo job executed at: {}", LocalDateTime.now());
        System.out.println(1);
    }
}
