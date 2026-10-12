package com.icit.controller;

import com.icit.model.SimpleChatRequest;
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

    public SimpleChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("/simple-chat")
    public ResponseEntity<String> chat(@RequestBody SimpleChatRequest simpleChatRequest){
        String resp= chatModel.chat(simpleChatRequest.getMessage());
        return ResponseEntity.ok(resp);
    }
}
