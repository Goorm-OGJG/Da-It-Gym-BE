package com.ogjg.daitgym.chat.dto;

import com.ogjg.daitgym.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private Long id;
    private String roomName;
    private String redisRoomId;
    private String sender;
    private String receiver;
    private String imageUrl;

    public static ChatRoomDto create(ChatMessageRequestDto messageRequestDto, User user) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.roomName = messageRequestDto.getReceiver();
        chatRoomDto.redisRoomId = UUID.randomUUID().toString();
        chatRoomDto.sender = user.getNickname();
        chatRoomDto.imageUrl = user.getImageUrl();
        chatRoomDto.receiver = messageRequestDto.getReceiver();

        return chatRoomDto;
    }
}
