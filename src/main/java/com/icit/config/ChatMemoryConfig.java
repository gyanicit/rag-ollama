package com.icit.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures per-user conversation memory for the {@code BabyAssistant}.
 *
 * <p>The provider creates a separate message window for each memory ID passed
 * by the assistant (the controller currently uses the user's ID). This keeps
 * one user's conversation history separate from another's.</p>
 */
@Configuration
public class ChatMemoryConfig {

    /**
     * Creates a bounded, in-memory chat history for each assistant user.
     * Keeping the most recent 20 messages gives the model conversational
     * context without allowing each request to resend an unbounded transcript.
     * The history is held in application memory and is lost when the app stops.
     *
     * @return provider selected by name in {@code BabyAssistant}'s {@code @AiService}
     */
    @Bean
    public ChatMemoryProvider userChatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .build();
    }
}
