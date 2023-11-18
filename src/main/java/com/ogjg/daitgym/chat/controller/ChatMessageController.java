package com.ogjg.daitgym.chat.controller;

import com.ogjg.daitgym.chat.dto.ChatMessageDto;
import com.ogjg.daitgym.chat.pubsub.RedisPublisher;
import com.ogjg.daitgym.chat.service.ChatMessageService;
import com.ogjg.daitgym.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService messageService;


    /**
     * websocket "/pub/chat/message/" 로 들어오는 메시지를 처리한다.
     * convertAndSend : Websocket 에 발행된 메시지를 redis 로 발행(publish)
     */
    @MessageMapping("/message")
    public void message(ChatMessageDto chatMessageDto, @Header("Authentication") String token) {

        chatRoomService.enterChatRoom(chatMessageDto.getRedisRoomId());

        chatMessageDto.setMessageCreatedAt(LocalDateTime.now());
        ChatMessageDto savedChatMessageDto = chatMessageDto;
        log.info("채팅 메시지");

        ChannelTopic topic = chatRoomService.getTopic(chatMessageDto.getRedisRoomId());
        if (!Objects.equals(chatMessageDto.getMessageType(), "ENTER")) {
            savedChatMessageDto = messageService.save(chatMessageDto, token);
        }
        redisPublisher.publish(topic, savedChatMessageDto);
    }
}
