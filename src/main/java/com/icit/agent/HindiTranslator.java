package com.icit.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface HindiTranslator {

    @UserMessage("""
            Translate the following story into natural, child-friendly Hindi.
            Preserve the meaning, events, and character names.
            Return only the Hindi story, with no explanation.

            Story:
            {{story}}
            """)
    @Agent("Translates an English story into Hindi")
    String translateToHindi(@V("story") String story);
}