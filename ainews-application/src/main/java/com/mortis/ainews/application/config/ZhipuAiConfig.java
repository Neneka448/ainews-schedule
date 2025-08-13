package com.mortis.ainews.application.config;

import ai.z.openapi.ZhipuAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ZhipuAiConfig {
    @Bean
    public ZhipuAiClient zhipuAiClient(@Value("${zhipu_apikey:}") String apikey) {
        return ZhipuAiClient.builder().apiKey(apikey).build();
    }
}
