package com.ogjg.daitgym.chat.service;

import com.ogjg.daitgym.chat.dto.*;
import com.ogjg.daitgym.chat.exception.NotFoundChattingRoom;
import com.ogjg.daitgym.chat.pubsub.RedisSubscriber;
import com.ogjg.daitgym.chat.repository.ChatRoomRepository;
import com.ogjg.daitgym.chat.repository.UsersChattingRoomRepository;
import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.UsersChattingRoom;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;
    private final UsersChattingRoomRepository usersChattingRoomRepository;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * 채팅방 생성
     * chatRoom이 존재하지 않거나,
     * chatRoom이 존재하지만, user의 nickname이 보내는 사람과 일치하지않고 받는 사람이 chatMessageRequestDto에서 받은 receiver과 일치하지 않을 때 생성한다.
     * 만약 채팅방이 존재한다면 존재하고 있는 chatRoomd의 redisRoomId 값을 return한다.
     */
    @Transactional
    public ChatRoomResponse createChatRoom(String email, ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
        ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(user.getNickname(), chatMessageRequestDto.getReceiver());

        if ((chatRoom == null) || (chatRoom != null && (!user.getNickname().equals(chatRoom.getSender()) && !chatMessageRequestDto.getReceiver().equals(chatRoom.getReceiver())))) {
            ChatRoomDto chatRoomDto = ChatRoomDto.create(chatMessageRequestDto, user);
            opsHashChatRoom.put(CHAT_ROOMS, chatRoomDto.getRedisRoomId(), chatRoomDto);

            ChatRoom saveChatRoom = ChatRoom.builder()
                    .chatRoomDto(chatRoomDto)
                    .user(user)
                    .build();

            chatRoomRepository.save(saveChatRoom);
            usersChattingRoomRepository.save(new UsersChattingRoom(user, saveChatRoom));
            return new ChatRoomResponse(saveChatRoom);

        } else {
            return new ChatRoomResponse(chatRoom.getRedisRoomId());
        }
    }

    /**
     * 사용자의 모든 채팅방 가져오기
     * 1. 채팅방에서 user의 nickName 과 sender가 동일하다면, roomName은 receiver로 설정한다. -> 113번째 줄
     * 2. 채팅방에서 user의 nockName과 receiver가 동일하다면, roomName은 sender로 설정한다. -> 123번째 줄
     * latestMessage : DB에 저장되어 있는 값 중에서 가장 최신의 값 추출
     */
    public List<ChatMessageResponseDto> findAllRoomByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderOrReceiver(user.getNickname());

        List<ChatMessageResponseDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatMessageDto latestMsg = chatMessageService.latestMessage(chatRoom.getRedisRoomId());
            String msg = (latestMsg != null) ? latestMsg.getMessage() : "";
            if (user.getNickname().equals(chatRoom.getSender())) {
                ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(
                        chatRoom.getId(),
                        chatRoom.getReceiver(),
                        chatRoom.getRedisRoomId(),
                        chatRoom.getSender(),
                        chatRoom.getReceiver(),
                        msg,
                        chatRoom.getImageUrl(),
                        chatRoom.getCreatedAt());

                chatRoomDtos.add(chatMessageResponseDto);
            } else if (user.getNickname().equals(chatRoom.getReceiver())) {
                ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(
                        chatRoom.getId(),
                        chatRoom.getSender(),
                        chatRoom.getRedisRoomId(),
                        chatRoom.getSender(),
                        chatRoom.getReceiver(),
                        msg,
                        chatRoom.getImageUrl(),
                        chatRoom.getCreatedAt()
                );

                chatRoomDtos.add(chatMessageResponseDto);
            }
        }
        return chatRoomDtos;
    }

    /**
     * 선택된 채팅방 가져오기
     * loadMessage : 채팅 목록 가져오기
     */

    public SelectedChatRoomResponse findRoom(String roomId, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
        ChatRoom chatRoom = chatRoomRepository.findByRedisRoomIdAndSenderOrRedisRoomIdAndReceiver(roomId, user.getNickname(), roomId, user.getNickname());
        List<ChatMessageDto> chatMessageDtos = chatMessageService.loadMessage(roomId, email);

        if (chatRoom == null) {
            throw new NotFoundChattingRoom("채팅방이 존재하지 않습니다.");
        }
        return new SelectedChatRoomResponse(chatRoom, chatMessageDtos);
    }


    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    /**
     * redis 채널에서 채팅방 조회
     */
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}