package com.ogjg.daitgym.chat.service;

import com.ogjg.daitgym.chat.dto.*;
import com.ogjg.daitgym.chat.pubsub.RedisSubscriber;
import com.ogjg.daitgym.chat.repository.ChatRoomRepository;
import com.ogjg.daitgym.chat.repository.UsersChattingRoomRepository;
import com.ogjg.daitgym.common.exception.chat.NotFoundChattingRoom;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.UsersChattingRoom;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    /**
     * topics : 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
     */
    private Map<String, ChannelTopic> topics;
    private final UserRepository userRepository;
    private final RedisSubscriber redisSubscriber;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final RedisMessageListenerContainer redisMessageListener;
    private final UsersChattingRoomRepository usersChattingRoomRepository;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    /**
     * 채팅방 생성
     * chatRoom이 존재하지 않거나,
     * 만약 채팅방이 존재한다면 존재하고 있는 chatRoomd의 redisRoomId 값을 return한다.
     */
    @Transactional
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest createChatRoomRequest, OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        String email = oAuth2JwtUserDetails.getEmail();
        User sender = getUserByEmail(email);
        User receiver = getUserNickname(createChatRoomRequest.getReceiver());
        List<UsersChattingRoom> usersChattingRooms = usersChattingRoomRepository.findChatRoomByEmails(sender.getEmail(), receiver.getEmail());

        if (usersChattingRooms.size() == 2) {
            return new ChatRoomResponse(usersChattingRooms.get(0).getChatRoom().getRedisRoomId());
        } else {

            ChatRoom saveChatRoom = new ChatRoom();
            chatRoomRepository.save(saveChatRoom);

            usersChattingRoomRepository.save(new UsersChattingRoom(sender, saveChatRoom));
            usersChattingRoomRepository.save(new UsersChattingRoom(receiver, saveChatRoom));

            return new ChatRoomResponse(saveChatRoom, sender, receiver);
        }
    }

    /**
     * 사용자의 모든 채팅방 가져오기
     * latestMessage : DB에 저장되어 있는 값 중에서 가장 최신의 값 추출
     */
    public List<ChatRoomsResponse> findAllRoomsByUser(OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        String email = oAuth2JwtUserDetails.getEmail();
        User user = getUserByEmail(email);

        List<UsersChattingRoom> usersChattingRooms = usersChattingRoomRepository.findAllByUser(user);
        List<ChatRoomsResponse> chatRoomDtos = new ArrayList<>();

        for (UsersChattingRoom usersChattingRoom : usersChattingRooms) {
            ChatRoom chatRoom = usersChattingRoom.getChatRoom();
            UsersChattingRoom ucr = usersChattingRoomRepository.findByChatRoomAndUserNot(chatRoom, user);
            User receiver = ucr.getUser();

            ChatMessageDto latestMsg = chatMessageService.latestMessage(chatRoom.getRedisRoomId());
            String msg = (latestMsg != null) ? latestMsg.getMessage() : "";
            LocalDateTime messageCreatedAt = (latestMsg != null) ? latestMsg.getMessageCreatedAt() : null;

            ChatRoomsResponse chatRoomsResponse = ChatRoomsResponse.builder().chatRoom(chatRoom).sender(user).receiver(receiver).message(msg).messageCreatedAt(messageCreatedAt).build();

            chatRoomDtos.add(chatRoomsResponse);
        }
        return chatRoomDtos;
    }

    /**
     * 선택된 채팅방 가져오기
     * loadMessage : 채팅 목록 가져오기
     */

    public SelectedChatRoomResponse findRoom(String redisRoomId,
                                             OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        String email = oAuth2JwtUserDetails.getEmail();
        User sender = getUserByEmail(email);
        ChatRoom chatRoom = chatRoomRepository.findByRedisRoomId(redisRoomId);
        if (chatRoom == null) {
            throw new NotFoundChattingRoom("채팅방이 존재하지 않습니다.");
        }
        UsersChattingRoom ucr = usersChattingRoomRepository.findByChatRoomAndUserNot(chatRoom, sender);


        User receiver = ucr.getUser();
        List<ChatMessageDto> chatMessageDtos = chatMessageService.loadMessage(redisRoomId, sender);

        return SelectedChatRoomResponse.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .messages(chatMessageDtos)
                .build();
    }


    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    /**
     * redis 채널에서 채팅방 조회
     */
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }

    private User getUserNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(NotFoundUser::new);
    }

}