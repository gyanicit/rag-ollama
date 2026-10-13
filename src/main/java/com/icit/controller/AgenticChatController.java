package com.icit.controller;

import com.icit.agent.CreativeWriter;
import com.icit.model.SimpleChatRequest;
import com.icit.workflow.StoryToHindiWorkflow;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AgenticChatController {

    private final CreativeWriter creativeWriter;

    public AgenticChatController(CreativeWriter creativeWriter) {
        this.creativeWriter = creativeWriter;
    }

    @PostMapping("/creative-writer")
    public ResponseEntity<String> chatWithCreativeWriter(@RequestBody SimpleChatRequest simpleChatRequest){
        String resp= creativeWriter.generateStory(simpleChatRequest.getMessage());
        return ResponseEntity.ok(resp);
    }
}
