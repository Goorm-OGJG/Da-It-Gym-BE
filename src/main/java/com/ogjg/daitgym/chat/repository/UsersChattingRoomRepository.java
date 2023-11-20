package com.ogjg.daitgym.chat.repository;

import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.UsersChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersChattingRoomRepository extends JpaRepository<UsersChattingRoom, Long> {
    @Query("select u from UsersChattingRoom u where u.user.email in (:senderEmail,:receiverEmail)" +
            "group by u.chatRoom having count(u) = 2")
    List<UsersChattingRoom> findChatRoomByEmails(@Param("senderEmail") String senderEmail, @Param("receiverEmail") String receiverEmail);

    List<UsersChattingRoom> findAllByUser(User user);

    UsersChattingRoom findByChatRoomAndUserNot(ChatRoom chatRoom, User user);
}
