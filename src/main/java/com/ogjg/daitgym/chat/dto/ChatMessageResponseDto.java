package com.ogjg.daitgym.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    private String message;
    private String imageUrl;
    private LocalDateTime messageCreatedAt;

    public ChatMessageResponseDto(Long id, String roomName, String redisRoomId, String sender, String receiver, String message, String imageUrl, LocalDateTime messageCreatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.redisRoomId = redisRoomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imageUrl = imageUrl;
        this.messageCreatedAt = messageCreatedAt;
    }
}