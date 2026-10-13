package com.icit.config;

import com.icit.agent.CreativeWriter;
import com.icit.agent.HindiTranslator;
import com.icit.workflow.StoryToHindiWorkflow;
import dev.langchain4j.agentic.AgenticServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfig {

    @Bean
    public StoryToHindiWorkflow storyToHindiWorkflow(CreativeWriter creativeWriter, HindiTranslator hindiTranslator) {

        return AgenticServices.sequenceBuilder(StoryToHindiWorkflow.class)
                .subAgents(creativeWriter, hindiTranslator)
                .outputKey("hindiStory")
                .build();
    }

}
