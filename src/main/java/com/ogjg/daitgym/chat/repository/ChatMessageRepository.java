package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop100ByRedisRoomIdOrderByCreatedAtAsc(String roomId);

    ChatMessage findTop1ByRedisRoomIdOrderByCreatedAtDesc(String roomId);

    ChatMessage findByRedisRoomIdAndId(String redisRoomId, Long chatMessageId);
}
