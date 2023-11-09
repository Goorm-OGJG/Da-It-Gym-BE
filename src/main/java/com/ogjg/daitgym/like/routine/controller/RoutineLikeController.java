package com.ogjg.daitgym.like.routine.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.like.routine.dto.RoutineLikeResponse;
import com.ogjg.daitgym.like.routine.service.RoutineLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routines/{routineId}/like")
@RequiredArgsConstructor
public class RoutineLikeController {

    private final RoutineLikeService routineLikeService;

    /**
     * TODO User 추가하기
     */
    @PostMapping
    public ApiResponse<RoutineLikeResponse> routineLike(@PathVariable Long routineId,
                                                        @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineLikeService.routineLike(routineId, oAuth2JwtUserDetails));
    }

    @DeleteMapping
    public ApiResponse<RoutineLikeResponse> routineUnLike(@PathVariable Long routineId,
                                                          @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineLikeService.routineUnLike(routineId, oAuth2JwtUserDetails));
    }
}
