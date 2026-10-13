package com.icit.workflow;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface StoryToHindiWorkflow {
    @Agent
    String generateStoryInHindi(@V("topic") String topic);
}
