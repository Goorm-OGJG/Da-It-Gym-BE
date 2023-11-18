package com.ogjg.daitgym.config.websocket;

import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
class StompHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private Map<String, String> sessionId;
    private HashOperations<String, String, String> hashOperations;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
        sessionId = new HashMap<>();
    }

    /**
     * Websocket 연결 시 요청 header 의 jwt token 유효성을 검증하는 코드를 추가한다. 유효하지 않은 JWT 토큰일 경우, websocket을 연결하지 않고 예외 처리 한다.
     * headerAccessor : Websocket 프로토콜에서 사용되는 헤더 정보를 추출하기 위해 stompHeaderAccessor 를 사용하여 메시지를 매핑한다.
     * presend() : 메시지가 실제로 채널에 전송되기전에 호출된다. 즉, publisher가 send 하기 전에 호출된다.
     */

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String session = (String) headerAccessor.getHeader("simpSessionId");
        handleMessage(headerAccessor.getCommand(), headerAccessor, session);

        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor headerAccessor, String session) {
        switch (stompCommand) {

            case CONNECT:
                break;
            case SUBSCRIBE:
                verifyAccessToken(headerAccessor);
                connectToChatRoom(headerAccessor, session);
                break;
            case DISCONNECT:
                disConnectToChatRoom(session);
                break;
        }
    }

    /**
     * 토큰검증로직
     */
    private boolean verifyAccessToken(StompHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("Authentication");
        String tokenStompHeader = jwtUtils.getTokenStompHeader(token);
        return jwtUtils.validateToken(tokenStompHeader);
    }

    /**
     * CONNET 상태 일때, redis에 SessionId값으로 redisRoomId와 email값을 저장한다.
     * 인원수를 알기 위해 redisRoomId를 key로 가지고 email을 value로 가지는 setOperations를 저장한다.
     * 연결되었을 때, 2로 설정되어있던 인원수를 1로 업데이트해준다.
     */
    private void connectToChatRoom(StompHeaderAccessor headerAccessor, String session) {
        ChannelTopic redisRoomId = ChannelTopic.of(headerAccessor.getFirstNativeHeader("RedisRoomId"));
        String stringRedisRoomID = redisRoomId.toString();
        String token = headerAccessor.getFirstNativeHeader("Authentication");
        String tokenStompHeader = jwtUtils.getTokenStompHeader(token);
        String email = jwtUtils.getEmail(tokenStompHeader);


        hashOperations.put(session, "RedisRoomId", stringRedisRoomID);
        hashOperations.put(session, "email", email);

        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(stringRedisRoomID + "set", email);
    }

    /**
     * disConnect 일때는 값을 보낼 수가 없어서 connect일 때 저장한 session값으로 redisRoomId와 email을 찾는다.
     * redisRoomId 와 email을 가지고 SetOperations에 저장된 값을 하나 지운다.
     * 삭제로 인해,  redisRoomId 에 들어있는 인원이 한명 줄어들었음을 알 수 있다.
     */

    private void disConnectToChatRoom(String session) {

        String redisRoomId = (String) redisTemplate.opsForHash().get(session, "RedisRoomId");
        String email = (String) redisTemplate.opsForHash().get(session, "email");
        redisTemplate.opsForSet().remove(redisRoomId + "set", email);
    }
}

