package com.ogjg.daitgym.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ogjg.daitgym.domain.ChatMessage;
import com.ogjg.daitgym.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class ChatMessageDto {

    private String messageType;
    private Long chatMessageId;
    private String sender;
    private String message;
    private String redisRoomId;
    private int readCount;
    private String imageUrl;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageCreatedAt;

    public ChatMessageDto(ChatMessage chatMessage, User sender) {
        this.chatMessageId = chatMessage.getId();
        this.redisRoomId = chatMessage.getRedisRoomId();
        this.sender = sender.getNickname();
        this.message = chatMessage.getMessage();
        this.readCount = chatMessage.getReadCount();
        this.imageUrl = sender.getImageUrl();
        this.messageCreatedAt = chatMessage.getMessageCreatedAt();
    }


    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
