package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatMessage;
import com.ogjg.daitgym.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findTop1ByRedisRoomIdOrderByMessageCreatedAtDesc(String roomId);

    List<ChatMessage> findAllByRedisRoomIdOrderByMessageCreatedAtAsc(String roomId);

    List<ChatMessage> findAllByRedisRoomIdAndReadCountAndUserNot(String redisRoomId, int i, User user);
}
