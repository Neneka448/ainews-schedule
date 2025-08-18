package com.mortis.ainews.application.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring AI 配置类
 * 配置各种AI模型的集成，包括OpenAI、Ollama等
 */
@Configuration
public class SpringAiConfig {

    @Value("${spring.ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.base-url:https://api.openai.com}")
    private String openAiBaseUrl;

    @Bean(name = "geminiFlashChatModel")
    public ChatModel geminiFlashChatModel() {
        return OpenAiChatModel
            .builder()
            .openAiApi(OpenAiApi
                .builder()
                .apiKey(openAiApiKey)
                .baseUrl(openAiBaseUrl)
                .build())
            .defaultOptions(OpenAiChatOptions
                .builder()
                .model("google/gemini-2.5-flash")
                .build())
            .build();
    }

    @Bean
    public ChatClient chatClient(@Qualifier("geminiFlashChatModel") ChatModel chatModel) {
        return ChatClient
            .builder(chatModel)
            .build();
    }
}
