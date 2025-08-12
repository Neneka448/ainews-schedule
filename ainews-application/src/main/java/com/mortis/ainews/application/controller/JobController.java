package com.mortis.ainews.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mortis.ainews.application.service.jobs.EchoService;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.jobrunr.scheduling.JobScheduler;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JobController {

}
