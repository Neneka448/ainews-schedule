package com.mortis.ainews.application.service.facility;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.core.Constants;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import ai.z.openapi.service.web_search.WebSearchRequest;
import ai.z.openapi.service.web_search.WebSearchResp;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZhipuAIService {
    private final ZhipuAiClient zhipuAiClient;


    public List<WebSearchResp> search(WebSearchRequest req) {
        var res = zhipuAiClient
            .webSearch()
            .createWebSearch(req);

        if (res.isSuccess()) {
            return res
                .getData()
                .getWebSearchResp();
        } else {
            return List.of();
        }
    }

    public String chat(String input) {
        ChatCompletionCreateParams request = ChatCompletionCreateParams
            .builder()
            .model(Constants.ModelChatGLM4_5)
            .messages(
                Collections.singletonList(
                    ChatMessage
                        .builder()
                        .role(ChatMessageRole.USER.value())
                        .content(input)
                        .build()
                )
            )
            .build();
        ChatCompletionResponse response = zhipuAiClient
            .chat()
            .createChatCompletion(request);

        if (response.isSuccess()) {
            Object reply = response
                .getData()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
            return reply.toString();
        } else {
            return response.getMsg();
        }
    }

}
