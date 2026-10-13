package com.icit.controller;

import com.icit.agent.CreativeWriter;
import com.icit.model.SimpleChatRequest;
import com.icit.workflow.StoryToHindiWorkflow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WorkflowChatController {

    private final StoryToHindiWorkflow storyToHindiWorkflow;

    public WorkflowChatController(StoryToHindiWorkflow storyToHindiWorkflow) {
        this.storyToHindiWorkflow = storyToHindiWorkflow;
    }

    @PostMapping("/story-hindi")
    public ResponseEntity<String> generateStoryInHindi(@RequestBody SimpleChatRequest request) {
        String hindiStory = storyToHindiWorkflow.generateStoryInHindi(request.getMessage());
        return ResponseEntity.ok(hindiStory);
    }
}
