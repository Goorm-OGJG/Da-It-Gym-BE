package com.ogjg.daitgym.chat.controller;

import com.ogjg.daitgym.chat.dto.CreateChatRoomRequest;
import com.ogjg.daitgym.chat.dto.ChatMessageResponseDto;
import com.ogjg.daitgym.chat.dto.ChatRoomResponse;
import com.ogjg.daitgym.chat.dto.SelectedChatRoomResponse;
import com.ogjg.daitgym.chat.service.ChatRoomService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse<ChatRoomResponse> createRoom(@RequestParam String email,
                                                    @RequestBody CreateChatRoomRequest createChatRoomRequest) {

        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.createChatRoom(email, createChatRoomRequest));
    }

    /**
     * 사용자 관련 모든 채팅방 조회
     */
    @GetMapping("/rooms")
    public ApiResponse<List<ChatMessageResponseDto>> findAllRoomByUser(@RequestParam String email) {
        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.findAllRoomByUser(email));
    }

    /**
     * 사용자 관련 선택된 채팅방 조회
     */
    @GetMapping("/rooms/{roomId}")
    public ApiResponse<SelectedChatRoomResponse> findRoom(@PathVariable String roomId,
                                                          @RequestParam String email) {
        return new ApiResponse<>(ErrorCode.SUCCESS, chatService.findRoom(roomId, email));

    }

}
