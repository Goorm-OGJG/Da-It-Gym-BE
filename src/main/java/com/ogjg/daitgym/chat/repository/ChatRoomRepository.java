package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.sender = :nickname OR cr.receiver = :nickname)")
    List<ChatRoom> findBySenderOrReceiver(String nickname);

    ChatRoom findBySenderAndReceiver(String nickName, String receiver);

    ChatRoom findByRedisRoomIdAndSenderOrRedisRoomIdAndReceiver(String roomId, String sender, String roomId1, String nickname);

    ChatRoom findByRedisRoomId(String redisRoomId);
}
