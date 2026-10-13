package com.icit.config;

import com.icit.agent.CreativeWriter;
import com.icit.agent.HindiTranslator;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {
    @Bean
    public CreativeWriter creativeWriter(ChatModel chatModel){
        return AgenticServices
                .agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputKey("story")
                .build();
    }

    @Bean
    public HindiTranslator hindiTranslator(ChatModel chatModel) {
        return AgenticServices.agentBuilder(HindiTranslator.class)
                .chatModel(chatModel)
                .outputKey("hindiStory")
                .build();
    }
}
