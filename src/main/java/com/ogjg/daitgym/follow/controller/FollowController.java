package com.ogjg.daitgym.follow.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.follow.dto.request.FollowRequest;
import com.ogjg.daitgym.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우 하기
     */
    @PostMapping
    public ApiResponse<Void> follow(
//            todo token에서 받아오기
            String email,
            @RequestBody FollowRequest followRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        followService.follow(email1, followRequest);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 언팔로우 하기
     */
    @DeleteMapping
    public ApiResponse<Void> unfollow(
//            todo token에서 받아오기
            String email,
            @RequestBody FollowRequest followRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        followService.unfollow(email1, followRequest);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
