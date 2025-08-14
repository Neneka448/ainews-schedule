package com.mortis.ainews.application.controller;


import com.mortis.ainews.application.dto.ScheduleCreateRequest;
import io.temporal.client.schedules.ScheduleClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleClient scheduleClient;


    @PostMapping("/create")
    public RequestEntity<String> createSchedule(@Valid @RequestBody ScheduleCreateRequest req) {
        

        return null;
    }


}
