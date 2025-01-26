package com.ogjg.daitgym.chat.service;

import com.ogjg.daitgym.chat.dto.ChatMessageDto;
import com.ogjg.daitgym.chat.repository.ChatMessageRepository;
import com.ogjg.daitgym.chat.repository.ChatRoomRepository;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import com.ogjg.daitgym.domain.ChatMessage;
import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;

    /**
     * 메세지 저장하는 로직
     * ReadCount 란, 메세지를 읽은 사람의 수를 나타낸다.
     * 처음에는 2로 값을 세팅하고, sub에 접속될 때 -1를 한다.
     * 따라서 메신저를 보내는 사람은 pub으로 메신저를 보내고, sub에 접속한 상태이므로 readCount는 1이된다.
     * 이때 상대방도 sub이 되면 readCount는 0이 되므로, readCount가 0일 때 읽음 표시를 해주면 된다.
     */

    @Transactional
    public ChatMessageDto save(ChatMessageDto chatMessageDto, String token) {
        String email = JwtUtils.extractEmail(token);
        User user = getUserByEmail(email);

        ChatRoom chatroom = chatRoomRepository.findByRedisRoomId(chatMessageDto.getRedisRoomId());

        int size = Math.toIntExact(redisTemplate.opsForSet().size(chatroom.getRedisRoomId() + "set"));

        if (size == 2) {
            chatMessageDto.setReadCount(0);
        } else {
            chatMessageDto.setReadCount(1);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .user(user)
                .chatRoom(chatroom)
                .message(chatMessageDto.getMessage())
                .redisRoomId(chatMessageDto.getRedisRoomId())
                .readCount(chatMessageDto.getReadCount())
                .build();

        chatMessageRepository.save(chatMessage);
        chatMessageDto.setChatMessageId(chatMessage.getId());
        chatMessageDto.setImageUrl(user.getImageUrl());
        chatMessageDto.setSender(user.getNickname());

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        redisTemplateMessage.opsForList().rightPush(chatMessageDto.getRedisRoomId(), chatMessageDto);
        redisTemplateMessage.expire(chatMessageDto.getRedisRoomId(), 60, TimeUnit.MINUTES);
        return chatMessageDto;
    }

    /**
     * 전체 메세지 로드하기
     * Connect 되어 두사람이 채팅방에 있을 때, readCount가 0이 되어야하기 때문에 redis에 저장된 readCount값이 1이라면 0으로 바꿔준다.
     * Long size 란 채팅방에 접속해있는 인원을 의미한다.
     */
    @Transactional
    public List<ChatMessageDto> loadMessage(String redisRoomId, User user) {

        updateReadCount(redisRoomId, user);

        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        List<ChatMessage> dbMessageList = chatMessageRepository.findAllByRedisRoomIdOrderByMessageCreatedAtAsc(redisRoomId);

        for (ChatMessage chatMessage : dbMessageList) {
            User sender = chatMessage.getUser();
            chatMessageDtos.add(new ChatMessageDto(chatMessage, sender));
        }
        return chatMessageDtos;
    }

    /**
     * 채팅 목록 가져올 때, 가장 최신 메시지 하나만 보여주기 위한 로직
     */
    @Transactional
    public ChatMessageDto latestMessage(String redisRoomId) {

        ChatMessageDto latestMessage = redisTemplateMessage.opsForList().index(redisRoomId, -1);

        if (latestMessage == null) {
            ChatMessage dbLatestMessage = chatMessageRepository.findTop1ByRedisRoomIdOrderByMessageCreatedAtDesc(redisRoomId);

            if (dbLatestMessage != null) {
                latestMessage = new ChatMessageDto(dbLatestMessage, dbLatestMessage.getUser());
                redisTemplateMessage.opsForList().rightPush(redisRoomId, latestMessage);
            }
        }
        return latestMessage;
    }

    @Transactional
    public void updateReadCount(String redisRoomId, User user) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRedisRoomIdAndReadCountAndUserNot(redisRoomId, 1, user);

        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setReadCount(0);
            chatMessageRepository.save(chatMessage);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }
}
