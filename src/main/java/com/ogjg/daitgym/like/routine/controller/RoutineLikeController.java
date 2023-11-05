package com.ogjg.daitgym.like.routine.controller;

import com.ogjg.daitgym.like.routine.dto.RoutineLikeResponse;
import com.ogjg.daitgym.like.routine.service.RoutineLikeService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<RoutineLikeResponse> routineLike(@PathVariable Long routineId) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineLikeService.routineLike(routineId));
    }

    @DeleteMapping
    public ApiResponse<RoutineLikeResponse> routineUnLike(@PathVariable Long routineId) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineLikeService.routineUnLike(routineId));
    }
}
