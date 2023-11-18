package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByRedisRoomId(String redisRoomId);
}
