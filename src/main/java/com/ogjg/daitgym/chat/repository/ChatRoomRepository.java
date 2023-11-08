package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.sender = :nickName OR cr.receiver = :nickName)")
    List<ChatRoom> findBySenderOrReceiver(String nickName);

    ChatRoom findBySenderAndReceiver(String nickName, String receiver);

    ChatRoom findByRedisRoomIdAndSenderOrRedisRoomIdAndReceiver(String roomId, String sender, String roomId1, String nickName);

    ChatRoom findByRedisRoomId(String redisRoomId);
}
