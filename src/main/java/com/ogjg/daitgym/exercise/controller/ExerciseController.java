package com.ogjg.daitgym.exercise.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.exercise.dto.response.ExerciseListResponse;
import com.ogjg.daitgym.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    /**
     * 운동부위로 운동 검색
     */
    @GetMapping("/{part}")
    public ApiResponse<ExerciseListResponse> exerciseList(
            @PathVariable("part") String part
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS, exerciseService.exerciseLists(part)
        );
    }

}
