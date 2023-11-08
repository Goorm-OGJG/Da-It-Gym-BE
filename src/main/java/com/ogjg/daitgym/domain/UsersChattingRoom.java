package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class UsersChattingRoom {

    @EmbeddedId
    private UsersChattingRoomPk usersChattingRoomPk;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Embeddable
    @NoArgsConstructor(access = PROTECTED)
    @EqualsAndHashCode
    @Getter
    public static class UsersChattingRoomPk implements Serializable {
        private String email;
        private Long roomId;

        public UsersChattingRoomPk(String email, Long roomId) {
            this.email = email;
            this.roomId = roomId;
        }
    }
    public UsersChattingRoom(User user, ChatRoom chatRoom) {
        this.usersChattingRoomPk  = new UsersChattingRoomPk(user.getEmail(),chatRoom.getId());
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
