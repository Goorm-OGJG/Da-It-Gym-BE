package com.ogjg.daitgym.chat.dto;

import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class SelectedChatRoomResponse {

    private Long id;
    private String roomName;
    private String redisRoomId;
    private String sender;
    private String receiver;
    private List<ChatMessageDto> messages;

    @Builder
    public SelectedChatRoomResponse(ChatRoom chatRoom, User sender, User receiver, List<ChatMessageDto> messages) {
        this.id = chatRoom.getId();
        this.roomName = receiver.getNickname();
        this.redisRoomId = chatRoom.getRedisRoomId();
        this.sender = sender.getNickname();
        this.receiver = receiver.getNickname();
        this.messages = messages;
    }
}