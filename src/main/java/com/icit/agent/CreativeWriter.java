package com.icit.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * Without AiService we can write an agent. Please refer below code inside
 * AgentConfig file.
 *     @Bean
 *     public CreativeWriter creativeWriter(ChatModel chatModel){
 *         return AgenticServices
 *                 .agentBuilder(CreativeWriter.class)
 *                 .chatModel(chatModel)
 *                 .outputKey("story")
 *                 .build();
 *     }
 *
 *     For this approach you need to have below additional dependency.
 *     		<dependency>
 * 			    <groupId>dev.langchain4j</groupId>
 * 			    <artifactId>langchain4j-agentic</artifactId>
 * 			    <version>1.22.0-beta32</version> <!-- Added the -beta suffix -->
 * 		    </dependency>
 */
public interface CreativeWriter {

    @UserMessage("""
            You are a creative writer.
            Generate a draft of a story no more than
            3 sentences long around the given topic.
            Return only the story and nothing else.
            The topic is {{topic}}.
            """)
    @Agent("Generates a story based on the given topic")
    String generateStory(@V("topic") String topic);
}