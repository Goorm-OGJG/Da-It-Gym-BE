package com.ogjg.daitgym.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomResponse {
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    public ChatRoomResponse(ChatRoom chatRoom, User sender, User receiver) {
        this.id = chatRoom.getId();
        this.redisRoomId = chatRoom.getRedisRoomId();
        this.createdAt = chatRoom.getCreatedAt();
        this.sender = sender.getNickname();
        this.receiver = receiver.getNickname();
        this.roomName = receiver.getNickname();
    }

    public ChatRoomResponse(String redisRoomId) {
        this.redisRoomId = redisRoomId;
    }
}
