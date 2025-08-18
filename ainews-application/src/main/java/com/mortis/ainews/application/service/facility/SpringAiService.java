package com.mortis.ainews.application.service.facility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpringAiService {

    private final ChatClient chatClient;

    /**
     * 简单的聊天接口
     *
     * @param message 用户消息
     * @return AI回复
     */
    public String chat(String message) {
        try {
            log.info(
                "Sending chat message: {}",
                message
            );

            String response = chatClient
                .prompt()
                .user(message)
                .call()
                .content();

            if (response != null) {
                log.info(
                    "Received chat response length: {}",
                    response.length()
                );
            }
            return response;

        } catch (Exception e) {
            log.error(
                "Error in chat: {}",
                e.getMessage(),
                e
            );
            throw new RuntimeException(
                "Chat service error: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * 使用模板的聊天接口
     *
     * @param template  提示模板
     * @param variables 模板变量
     * @return AI回复
     */
    public String chatWithTemplate(String template, Map<String, Object> variables) {
        try {
            log.info(
                "Sending chat with template: {}, variables: {}",
                template,
                variables
            );

            PromptTemplate promptTemplate = new PromptTemplate(template);
            Prompt prompt = promptTemplate.create(variables);

            String response = chatClient
                .prompt(prompt)
                .call()
                .content();

            if (response != null) {
                log.info(
                    "Received templated chat response length: {}",
                    response.length()
                );
            }
            return response;

        } catch (Exception e) {
            log.error(
                "Error in templated chat: {}",
                e.getMessage(),
                e
            );
            throw new RuntimeException(
                "Templated chat service error: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * 获取完整的聊天响应（包含元数据）
     *
     * @param message 用户消息
     * @return 完整的ChatResponse
     */
    public ChatResponse getChatResponse(String message) {
        try {
            log.info(
                "Getting full chat response for: {}",
                message
            );

            ChatResponse response = chatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse();

            if (response != null) {
                log.info(
                    "Received chat response with {} results",
                    response
                        .getResults()
                        .size()
                );
            }
            return response;

        } catch (Exception e) {
            log.error(
                "Error getting chat response: {}",
                e.getMessage(),
                e
            );
            throw new RuntimeException(
                "Chat response service error: " + e.getMessage(),
                e
            );
        }
    }
}
