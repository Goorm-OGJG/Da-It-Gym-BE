package com.ogjg.daitgym.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String sender;
    private String message;
    private String redisRoomId;
    private String imageUrl;
    private int readCount;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageCreatedAt;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(String sender, ChatRoom chatRoom, String message, String redisRoomId, int readCount,String imageUrl) {
        super();
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.message = message;
        this.redisRoomId = redisRoomId;
        this.readCount = readCount;
        this.imageUrl = imageUrl;
        this.messageCreatedAt = LocalDateTime.now();
    }
}
