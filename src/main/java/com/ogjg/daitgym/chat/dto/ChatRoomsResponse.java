package com.ogjg.daitgym.chat.dto;

import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomsResponse {
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    private String message;
    private String imageUrl;
    private LocalDateTime messageCreatedAt;

    @Builder
    public ChatRoomsResponse(ChatRoom chatRoom, User sender, User receiver, String message, LocalDateTime messageCreatedAt) {
        this.id = chatRoom.getId();
        this.redisRoomId = chatRoom.getRedisRoomId();
        this.roomName = receiver.getNickname();
        this.sender = sender.getNickname();
        this.receiver = receiver.getNickname();
        this.imageUrl = receiver.getImageUrl();
        this.message = message;
        this.messageCreatedAt = messageCreatedAt;
    }
}