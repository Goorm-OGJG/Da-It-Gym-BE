package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.chat.dto.ChatRoomDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @Builder
    public ChatRoom(ChatRoomDto chatRoomDto, User user) {
        this.roomName = chatRoomDto.getReceiver();
        this.sender = chatRoomDto.getSender();
        this.redisRoomId = chatRoomDto.getRedisRoomId();
        this.user = user;
        this.imageUrl = user.getImageUrl();
        this.receiver = chatRoomDto.getReceiver();
    }
}
