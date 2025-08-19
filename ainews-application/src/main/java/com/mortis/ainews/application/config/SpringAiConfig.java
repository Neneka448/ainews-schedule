package com.mortis.ainews.application.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Spring AI 配置类
 * 配置各种AI模型的集成，支持自定义OpenAI兼容端点
 * <p>
 * 支持的配置属性：
 * - spring.ai.openai.base-url: 自定义API基础URL（默认：https://api.openai.com/v1）
 * - spring.ai.openai.api-key: API密钥（必需）
 * - spring.ai.openai.chat.options.model: 模型名称（默认：gpt-4o-mini）
 * - spring.ai.openai.chat.options.temperature: 温度参数（默认：0.8）
 * - spring.ai.openai.chat.options.max-tokens: 最大令牌数
 * - spring.ai.openai.chat.options.frequency-penalty: 频率惩罚
 * - spring.ai.openai.chat.options.presence-penalty: 存在惩罚
 */
@Configuration
public class SpringAiConfig {

    /**
     * 创建OpenAI API客户端
     * 支持自定义基础URL，可用于OpenAI兼容的服务（如Azure OpenAI、本地部署的模型等）
     */
    @Bean
    public OpenAiApi openAiApi(
        @Value("${spring.ai.openai.base-url:https://api.openai.com/v1}") String baseUrl,
        @Value("${spring.ai.openai.api-key}") String apiKey)
    {
        // 验证必需的配置
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalArgumentException("OpenAI API key is required. Please set spring.ai.openai.api-key property.");
        }

        // 创建OpenAiApi实例，支持自定义基础URL
        return OpenAiApi
            .builder()
            .baseUrl(baseUrl)
            .apiKey(apiKey)
            .build();
    }

    /**
     * 创建OpenAI聊天模型
     * 支持丰富的配置选项，包括模型参数调优
     */
    @Bean
    public OpenAiChatModel openAiChatModel(
        OpenAiApi openAiApi,
        @Value("${spring.ai.openai.chat.options.model:gpt-4o-mini}") String model,
        @Value("${spring.ai.openai.chat.options.temperature:0.8}") Double temperature,
        @Value("${spring.ai.openai.chat.options.max-tokens:#{null}}") Integer maxTokens,
        @Value("${spring.ai.openai.chat.options.frequency-penalty:0.0}") Double frequencyPenalty,
        @Value("${spring.ai.openai.chat.options.presence-penalty:0.0}") Double presencePenalty)
    {
        // 构建聊天选项
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions
            .builder()
            .model(model)
            .temperature(temperature)
            .frequencyPenalty(frequencyPenalty)
            .presencePenalty(presencePenalty);

        // 添加可选参数
        if (maxTokens != null && maxTokens > 0) {
            optionsBuilder.maxTokens(maxTokens);
        }

        OpenAiChatOptions options = optionsBuilder.build();

        return OpenAiChatModel
            .builder()
            .openAiApi(openAiApi)
            .defaultOptions(options)
            .build();
    }

    /**
     * 创建聊天客户端
     * 提供高级聊天功能，包括对话管理、提示模板等
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient
            .builder(openAiChatModel)
            .build();
    }
}
