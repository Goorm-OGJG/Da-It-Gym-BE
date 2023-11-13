package com.ogjg.daitgym.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String receiver;
    private String receiverImageUrl;
}
