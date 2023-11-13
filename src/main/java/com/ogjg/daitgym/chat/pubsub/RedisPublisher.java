package com.ogjg.daitgym.chat.pubsub;

import com.ogjg.daitgym.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, ChatMessageDto> redisTemplate;

    /**
     * 메시지를 redis 서버로 발행
     */

    public void publish(ChannelTopic topic, ChatMessageDto chatMessageDto) {
        redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
    }
}