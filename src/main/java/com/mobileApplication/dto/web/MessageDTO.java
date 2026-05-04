package com.mobileApplication.dto.web;

import java.time.LocalDateTime;

public class MessageDTO {
	
	public Long id;
    public String sender;
    public String content;
    public LocalDateTime createdAt;

    public MessageDTO(Long id, String sender, String content, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }

}
