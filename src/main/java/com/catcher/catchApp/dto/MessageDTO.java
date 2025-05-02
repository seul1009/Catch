package com.catcher.catchApp.dto;

public class MessageDTO {
    private String sender;
    private String content;

    public MessageDTO(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}