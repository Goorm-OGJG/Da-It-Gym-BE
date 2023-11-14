package com.ogjg.daitgym.follow.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.follow.dto.response.FollowCountResponse;
import com.ogjg.daitgym.follow.dto.response.FollowListResponse;
import com.ogjg.daitgym.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PostMapping("/{nickname}")
    public ApiResponse<Void> follow(
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails,
            @PathVariable("nickname") String nickname
    ) {
        followService.follow(userDetails.getEmail(), nickname);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 언팔로우 하기
     */
    @DeleteMapping("/{nickname}")
    public ApiResponse<Void> unfollow(
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails,
            @PathVariable String nickname
    ) {
        followService.unfollow(userDetails.getEmail(), nickname);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 내가 팔로우한 유저 수
     */
    @GetMapping("/following-counts/{nickname}")
    public ApiResponse<FollowCountResponse> followingCount(
            @PathVariable("nickname") String nickname
    ) {
        return new ApiResponse<>(ErrorCode.SUCCESS, followService.followingCount(nickname));
    }

    /**
     * 나를 팔로우한 유저 수
     */
    @GetMapping("/follower-counts/{nickname}")
    public ApiResponse<FollowCountResponse> followerCount(
            @PathVariable("nickname") String nickname
    ) {
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
