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
    private int readCount = 2;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage( String sender, ChatRoom chatRoom, String message, String redisRoomId) {
        super();
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.message = message;
        this.redisRoomId = redisRoomId;
        this.createdAt = LocalDateTime.now();
    }


    public int setReadCount() {
        return --readCount;
    }

}
