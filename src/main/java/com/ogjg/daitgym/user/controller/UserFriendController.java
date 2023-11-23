package com.ogjg.daitgym.user.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.user.dto.response.KaKaoFriendsResponse;
import com.ogjg.daitgym.user.service.KakaoFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/kakao")
@RequiredArgsConstructor
public class UserFriendController {

    private final KakaoFriendService kakaoFriendService;

    /**
     * 카카오 친구 목록이 존재하면 응답값을 담아서 반환
     * 친구 목록이 존재하지 않는다면 빈배열을 반환
     */
    @GetMapping("/friends")
    public ApiResponse<KaKaoFriendsResponse> getKaKaoFriendsList(
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                kakaoFriendService.requestKaKaoFriendsList(userDetails.getEmail())
        );
    }
}
