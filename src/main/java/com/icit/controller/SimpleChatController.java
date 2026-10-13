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
public class SimpleChatController {

    private final ChatModel chatModel;
    private final CreativeWriter creativeWriter;
    private final StoryToHindiWorkflow storyToHindiWorkflow;

    public SimpleChatController(ChatModel chatModel, CreativeWriter creativeWriter, StoryToHindiWorkflow storyToHindiWorkflow) {
        this.chatModel = chatModel;
        this.creativeWriter = creativeWriter;
        this.storyToHindiWorkflow = storyToHindiWorkflow;
    }

    @PostMapping("/simple-chat")
    public ResponseEntity<String> chat(@RequestBody SimpleChatRequest simpleChatRequest){
        String resp= chatModel.chat(simpleChatRequest.getMessage());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/creative-writer")
    public ResponseEntity<String> chatWithCreativeWriter(@RequestBody SimpleChatRequest simpleChatRequest){
        String resp= creativeWriter.generateStory(simpleChatRequest.getMessage());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/story-hindi")
    public ResponseEntity<String> generateStoryInHindi(@RequestBody SimpleChatRequest request) {
        String hindiStory = storyToHindiWorkflow.generateStoryInHindi(request.getMessage());
        return ResponseEntity.ok(hindiStory);
    }
}
