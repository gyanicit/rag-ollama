package com.icit.model;

public class ChatRequest {
    //This is the id if logged-in user. LLM also usages this userId to
    // segregate the conversation history.
    private String userId;
    private String message;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
