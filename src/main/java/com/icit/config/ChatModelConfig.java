package com.icit.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
public class ChatModelConfig {

    @Bean
    public ChatModel chatModel(@Value("${chat-model.baseUrl}") String baseUrl) {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .modelName("llama3.2")
                .timeout(Duration.ofMinutes(5))
                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
                .maxRetries(0)
                .build();
    }
}
