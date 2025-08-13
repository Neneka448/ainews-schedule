package com.mortis.ainews.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mortis.ainews.application.service.facility.ZhipuAIService;

import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class JobController {

    private final ZhipuAIService zhipuAiService;

    @GetMapping("/test")
    public String test(@RequestParam String input) {
        return zhipuAiService.chat(input);
    }

}
