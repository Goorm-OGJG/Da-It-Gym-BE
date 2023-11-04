package com.ogjg.daitgym.follow.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.follow.dto.request.FollowRequest;
import com.ogjg.daitgym.follow.dto.response.FollowCountResponse;
import com.ogjg.daitgym.follow.dto.response.FollowListResponse;
import com.ogjg.daitgym.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
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

    /**
     * 내가 팔로우한 유저 수
     */
    @GetMapping("/following-count/{nickname}")
    public ApiResponse<FollowCountResponse> followingCount(
            @PathVariable("nickname") String nickname
    ) {
        String email1 = "dlehdwls21@naver.com";
        return new ApiResponse<>(ErrorCode.SUCCESS, followService.followingCount(nickname));
    }

    /**
     * 나를 팔로우한 유저 수
     */
    @GetMapping("/follower-count/{nickname}")
    public ApiResponse<FollowCountResponse> followerCount(
            @PathVariable("nickname") String nickname
    ) {
        String email1 = "dlehdwls21@naver.com";
        return new ApiResponse<>(ErrorCode.SUCCESS, followService.followerCount(nickname));
    }

    /**
     * 팔로워 목록 가져오기
     */
    @GetMapping("/follower-list/{nickname}")
    public ApiResponse<FollowListResponse> followerList(
            @PathVariable("nickname") String nickname
    ) {
        return new ApiResponse<>(ErrorCode.SUCCESS, followService.followerList(nickname));
    }

    /**
     * 팔로잉 목록 가져오기
     */
    @GetMapping("/following-list/{nickname}")
    public ApiResponse<FollowListResponse> followingList(
            @PathVariable("nickname") String nickname
    ) {
        return new ApiResponse<>(ErrorCode.SUCCESS, followService.followingList(nickname));
    }
}
