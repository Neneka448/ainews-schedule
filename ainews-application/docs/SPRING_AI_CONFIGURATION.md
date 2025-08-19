# Spring AI Configuration Guide

This document provides comprehensive guidance on configuring Spring AI with custom ChatModel implementations, specifically for OpenAI-compatible endpoints.

## Overview

The `SpringAiConfig.java` class has been enhanced to support:
- Custom OpenAI-compatible endpoints (custom base URL and API key)
- OpenAI protocol-compatible services (not just official OpenAI)
- Rich configuration options for model parameters
- Best practices for production deployments

## Configuration Properties

### Required Properties

```properties
# API Key (Required)
spring.ai.openai.api-key=your-api-key-here
```

### Optional Properties

```properties
# Base URL Configuration (Default: https://api.openai.com/v1)
spring.ai.openai.base-url=https://api.openai.com/v1

# Organization and Project IDs (Optional)
spring.ai.openai.organization-id=your-org-id
spring.ai.openai.project-id=your-project-id

# Model Configuration
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.8
spring.ai.openai.chat.options.max-tokens=2048
spring.ai.openai.chat.options.top-p=1.0
spring.ai.openai.chat.options.frequency-penalty=0.0
spring.ai.openai.chat.options.presence-penalty=0.0
```

## Custom Endpoint Examples

### Azure OpenAI Service

```properties
spring.ai.openai.base-url=https://your-resource-name.openai.azure.com/openai/deployments/your-deployment-name
spring.ai.openai.api-key=your-azure-api-key
spring.ai.openai.chat.options.model=gpt-4
```

### Local OpenAI-Compatible Service (e.g., Ollama, LocalAI)

```properties
spring.ai.openai.base-url=http://localhost:11434/v1
spring.ai.openai.api-key=not-needed-for-local
spring.ai.openai.chat.options.model=llama2
```

### Third-Party OpenAI-Compatible Services

```properties
# Example: Groq
spring.ai.openai.base-url=https://api.groq.com/openai/v1
spring.ai.openai.api-key=your-groq-api-key
spring.ai.openai.chat.options.model=mixtral-8x7b-32768

# Example: Perplexity AI
spring.ai.openai.base-url=https://api.perplexity.ai
spring.ai.openai.api-key=your-perplexity-api-key
spring.ai.openai.chat.options.model=llama-3.1-sonar-small-128k-online
```

## Configuration Best Practices

### 1. Environment-Specific Configuration

Use different configuration files for different environments:

**application-dev.properties:**
```properties
spring.ai.openai.base-url=http://localhost:11434/v1
spring.ai.openai.api-key=dev-key
spring.ai.openai.chat.options.model=llama2
spring.ai.openai.chat.options.temperature=0.9
```

**application-prod.properties:**
```properties
spring.ai.openai.base-url=https://api.openai.com/v1
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o
spring.ai.openai.chat.options.temperature=0.7
```

### 2. Security Best Practices

- **Never hardcode API keys** in configuration files
- Use environment variables or secure configuration management
- Consider using Spring Cloud Config for centralized configuration

```properties
# Use environment variables
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Or use Spring Expression Language (SpEL)
spring.ai.openai.api-key=#{environment.getProperty('OPENAI_API_KEY')}
```

### 3. Model Parameter Tuning

#### Temperature Settings
- **0.0-0.3**: Deterministic, focused responses (good for factual queries)
- **0.4-0.7**: Balanced creativity and consistency (general purpose)
- **0.8-1.0**: Creative, varied responses (creative writing, brainstorming)

#### Token Management
```properties
# Set appropriate limits based on your use case
spring.ai.openai.chat.options.max-tokens=1024  # For short responses
spring.ai.openai.chat.options.max-tokens=4096  # For detailed responses
```

### 4. Error Handling and Retry Configuration

```properties
# Retry configuration
spring.ai.retry.max-attempts=3
spring.ai.retry.backoff.initial-interval=2s
spring.ai.retry.backoff.multiplier=2
spring.ai.retry.backoff.max-interval=10s
```

## Advanced Configuration

### Multiple ChatModel Instances

You can create multiple ChatModel instances for different purposes:

```java
@Configuration
public class MultiModelConfig {
    
    @Bean
    @Qualifier("creative")
    public OpenAiChatModel creativeChatModel(OpenAiApi openAiApi) {
        return new OpenAiChatModel(openAiApi, 
            OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.9f)
                .build());
    }
    
    @Bean
    @Qualifier("analytical")
    public OpenAiChatModel analyticalChatModel(OpenAiApi openAiApi) {
        return new OpenAiChatModel(openAiApi,
            OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.1f)
                .build());
    }
}
```

### Custom API Key Management

```java
@Bean
public OpenAiApi customOpenAiApi() {
    // Custom API key retrieval logic
    String apiKey = retrieveApiKeyFromVault();
    
    return OpenAiApi.builder()
        .baseUrl("https://api.openai.com/v1")
        .apiKey(apiKey)
        .build();
}
```

## Testing Configuration

### Unit Testing

```java
@TestConfiguration
public class TestSpringAiConfig {
    
    @Bean
    @Primary
    public OpenAiApi mockOpenAiApi() {
        return Mockito.mock(OpenAiApi.class);
    }
}
```

### Integration Testing

```properties
# application-test.properties
spring.ai.openai.base-url=http://localhost:8080/mock-openai
spring.ai.openai.api-key=test-key
spring.ai.openai.chat.options.model=test-model
```

## Troubleshooting

### Common Issues

1. **Connection Refused**: Check if the base URL is correct and accessible
2. **Authentication Failed**: Verify API key and organization/project IDs
3. **Model Not Found**: Ensure the model name is supported by the endpoint
4. **Rate Limiting**: Configure appropriate retry settings

### Debugging

Enable debug logging:

```properties
logging.level.org.springframework.ai.openai=DEBUG
logging.level.org.springframework.web.reactive.function.client=DEBUG
```

## Migration from Existing Configuration

If you're migrating from the previous configuration, update your properties:

```properties
# Old (still supported)
spring.ai.openai.chat.options.model=gpt-4o-mini

# Enhanced (recommended)
spring.ai.openai.base-url=https://api.openai.com/v1
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.8
```

## Performance Considerations

1. **Connection Pooling**: Spring AI uses WebClient with connection pooling by default
2. **Timeout Configuration**: Configure appropriate timeouts for your use case
3. **Async Processing**: Consider using reactive streams for high-throughput scenarios

```properties
# WebClient timeout configuration
spring.ai.openai.client.connect-timeout=10s
spring.ai.openai.client.read-timeout=60s
```
