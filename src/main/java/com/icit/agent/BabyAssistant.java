package com.icit.agent;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "chatModel",
        chatMemoryProvider = "userChatMemoryProvider",
        retrievalAugmentor = "ragRetrievalAugmentor",
        tools = {"babyProductTool", "vaccineTool"}
)
public interface BabyAssistant {

    @SystemMessage("""
            You are a helpful baby care assistant.
            
            You have access to:
            - Baby product catalog with pricing and stock info
            - Complete vaccine schedule for infants and toddlers
            
            Rules:
            - Use provided context from documents to answer questions
            - For product questions: always mention price, availability
            - For vaccine questions: always mention recommended age, do… [cut off at right edge]
            - If information is not in context, say so honestly
            - Always be warm, friendly and reassuring for new parents
            """)
    String chat(@MemoryId String userId, @UserMessage String userMessage);
}
