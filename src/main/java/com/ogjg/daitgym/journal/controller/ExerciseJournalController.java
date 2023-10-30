package com.ogjg.daitgym.journal.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.journal.dto.request.CreateJournalRequest;
import com.ogjg.daitgym.journal.dto.request.ExerciseHistoryRequest;
import com.ogjg.daitgym.journal.dto.request.ExerciseListRequest;
import com.ogjg.daitgym.journal.service.ExerciseJournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class ExerciseJournalController {

    private final ExerciseJournalService exerciseJournalService;

    /**
     * 빈 일지 생성하기
     */
    @PostMapping
    public ApiResponse<Void> createJournal(
//            todo 토큰에서 유저이메일 받아오기
            String email,
            @RequestBody CreateJournalRequest createJournalRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.createJournal(email1, createJournalRequest.getJournalDate());

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일지에 운동 추가하기
     */
    @PostMapping("/exercise-list")
    public ApiResponse<Void> addExerciseToJournal(
//            todo 토큰에서 유저이메일 받아오기
            String email,
            @RequestBody ExerciseListRequest exerciseListRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.createExerciseList(email1, exerciseListRequest);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 운동 목록에 운동기록 추가하기
     */
    @PostMapping("/exercise-history")
    public ApiResponse<Void> addExerciseHistoryToExerciseList(
//            todo 토큰에서 유저이메일 받아오기
            String email,
            @RequestBody ExerciseHistoryRequest exerciseHistoryRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.createExerciseHistory(email1, exerciseHistoryRequest);

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
