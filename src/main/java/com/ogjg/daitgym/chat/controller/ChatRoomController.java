package com.ogjg.daitgym.chat.controller;

import com.ogjg.daitgym.chat.dto.CreateChatRoomRequest;
import com.ogjg.daitgym.chat.dto.ChatRoomsResponse;
import com.ogjg.daitgym.chat.dto.ChatRoomResponse;
import com.ogjg.daitgym.chat.dto.SelectedChatRoomResponse;
import com.ogjg.daitgym.chat.service.ChatRoomService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {


    private final ChatRoomService chatService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/rooms")
    public ApiResponse<ChatRoomResponse> createRoom(@RequestBody CreateChatRoomRequest createChatRoomRequest,
                                                    @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.createChatRoom(createChatRoomRequest, oAuth2JwtUserDetails));
    }

    /**
     * 사용자 관련 모든 채팅방 조회
     */
    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomsResponse>> findAllRoomsByUser(@AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.findAllRoomsByUser(oAuth2JwtUserDetails));
    }

    /**
     * 사용자 관련 선택된 채팅방 조회
     */
    @GetMapping("/rooms/{redisRoomId}")
    public ApiResponse<SelectedChatRoomResponse> findRoom(@PathVariable String redisRoomId,
                                                          @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.findRoom(redisRoomId, oAuth2JwtUserDetails));

    }

}
