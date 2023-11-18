package com.ogjg.daitgym.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomDto {

    private Long id;
    private String redisRoomId;
    private String roomName;
    private String sender;
    private String receiver;
    private String imageUrl;
}
