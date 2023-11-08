package com.ogjg.daitgym.chat.service;

import com.ogjg.daitgym.chat.dto.ChatMessageDto;
import com.ogjg.daitgym.chat.exception.NotFoundChattingRoom;
import com.ogjg.daitgym.chat.repository.ChatMessageRepository;
import com.ogjg.daitgym.chat.repository.ChatRoomRepository;
import com.ogjg.daitgym.domain.ChatMessage;
import com.ogjg.daitgym.domain.ChatRoom;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;

    /**
     * 메세지 저장하는 로직
     * ReadCount 란, 메세지를 읽은 사람의 수를 나타낸다.
     * 처음에는 2로 값을 세팅하고, sub에 접속될 때 -1를 한다.
     * 따라서 메신저를 보내는 사람은 pub으로 메신저를 보내고, sub에 접속한 상태이므로 readCount는 1이된다.
     * 이때 상대방도 sub이 되면 readCount는 0이 되므로, readCount가 0일 때 읽음 표시를 해주면 된다.
     */

    public ChatMessageDto save(ChatMessageDto chatMessageDto) {

        ChatRoom chatroom = chatRoomRepository.findByRedisRoomId(chatMessageDto.getRedisRoomId());
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(chatMessageDto.getSender())
                .chatRoom(chatroom)
                .message(chatMessageDto.getMessage())
                .redisRoomId(chatMessageDto.getRedisRoomId())
                .build();
        chatMessageRepository.save(chatMessage);
        chatMessageDto.setChatMessageId(chatMessage.getId());
        chatMessageDto.setReadCount(2);
        return chatMessageDto;
    }

    /**
     * 전체 메세지 로드하기
     * 1. 먼저 Redisd에 저장된 100개의 값을 가져온다.
     * 2. 만약 Redis에 값이 비어있다면, chatMessageRepository에서 100개까지의 값을 가져온다.
     * 3. 그리고 메세지가 로드되었을 때, readCount값을 추가적으로 설정해주어야 한다.
     * 4. 로드된 메세지의 readCouont값이 1일 때, 메신저를 보낸 사람과 유저가 같지 않다면 읽었다는 의미로 1의 값을 0으로 세팅해줘야 한다.
     */
    public List<ChatMessageDto> loadMessage(String roomId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
        String nickName = user.getNickname();

        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();

        List<ChatMessageDto> redisMessageList = redisTemplateMessage.opsForList().range(roomId, 0, 99);

        if (redisMessageList == null || redisMessageList.isEmpty() || redisMessageList.size()<50) {
            List<ChatMessage> dbMessageList = chatMessageRepository.findTop100ByRedisRoomIdOrderByCreatedAtAsc(roomId);

            for (ChatMessage chatMessage : dbMessageList) {
                ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage);
                chatMessageDtos.add(chatMessageDto);

                redisTemplateMessage.opsForList().rightPush(roomId, chatMessageDto);
            }
        } else {
            chatMessageDtos.addAll(redisMessageList);
        }

        List<ChatMessageDto> modifiedChatMessageDtos = new ArrayList<>(chatMessageDtos);

        Iterator<ChatMessageDto> iterator = modifiedChatMessageDtos.iterator();
        while (iterator.hasNext()) {
            ChatMessageDto chatMessageDto = iterator.next();
            Long chatMessageId = chatMessageDto.getChatMessageId();
            String redisRoomId = chatMessageDto.getRedisRoomId();
            ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId).orElseThrow(NotFoundChattingRoom::new);

            if (!nickName.equals(chatMessageDto.getSender())) {
                if (chatMessageDto.getReadCount() == 1) {
                    chatMessageDto.setReadCount(chatMessageDto.getReadCount() - 1);

                    redisTemplateMessage.opsForList().set(redisRoomId, chatMessageDtos.indexOf(chatMessageDto), chatMessageDto);
                    chatMessage.setReadCount(0);
                    chatMessageRepository.save(chatMessage);

                }
            }
        }

        return modifiedChatMessageDtos;
    }

    /**
     * 채팅 목록 가져올 때, 가장 최신 메시지 하나만 보여주기 위한 로직
     */

    public ChatMessageDto latestMessage(String roomId) {

        ChatMessageDto latestMessage = redisTemplateMessage.opsForList().index(roomId, -1);

        if (latestMessage == null) {
            ChatMessage dbLatestMessage = chatMessageRepository.findTop1ByRedisRoomIdOrderByCreatedAtDesc(roomId);

            if (dbLatestMessage != null) {
                latestMessage = new ChatMessageDto(dbLatestMessage);
                redisTemplateMessage.opsForList().rightPush(roomId, latestMessage);
            }
        }
        return latestMessage;
    }
}