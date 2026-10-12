package com.icit.controller;

import com.icit.agent.BabyAssistant;
import com.icit.model.ChatRequest;
import com.icit.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BabyAssistantController {

    private final BabyAssistant babyAssistant;


    public BabyAssistantController(BabyAssistant babyAssistant) {
        this.babyAssistant = babyAssistant;
    }

    @PostMapping("/rag-chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest){
        String resp= babyAssistant.chat(chatRequest.getUserId(), chatRequest.getMessage());
        return ResponseEntity.ok(new ChatResponse(resp));
    }
}
