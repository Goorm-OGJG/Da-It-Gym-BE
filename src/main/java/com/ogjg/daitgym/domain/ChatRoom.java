package com.ogjg.daitgym.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ogjg.daitgym.chat.dto.ChatRoomDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    private String imageUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @Builder
    public ChatRoom(ChatRoomDto chatRoomDto, User user) {
        this.roomName = chatRoomDto.getReceiver();
        this.sender = chatRoomDto.getSender();
        this.redisRoomId = chatRoomDto.getRedisRoomId();
        this.user = user;
        this.imageUrl = chatRoomDto.getImageUrl();
        this.receiver = chatRoomDto.getReceiver();
        this.createdAt = LocalDateTime.now();
    }
}
