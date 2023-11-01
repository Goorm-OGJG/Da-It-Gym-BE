package com.ogjg.daitgym.journal.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.journal.dto.request.*;
import com.ogjg.daitgym.journal.dto.response.UserJournalDetailResponse;
import com.ogjg.daitgym.journal.dto.response.UserJournalListResponse;
import com.ogjg.daitgym.journal.service.ExerciseJournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    /**
     * 운동 일지 삭제하기
     */
    @DeleteMapping("/{journalId}")
    public ApiResponse<Void> deleteJournal(
            //todo email가져오기
            String email,
            @PathVariable("journalId") Long journalId
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.deleteJournal(email1, journalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 운동 목록 삭제하기
     */
    @DeleteMapping("/exercise-list/{exerciseListId}")
    public ApiResponse<Void> deleteExerciseList(
            //            todo 토큰에서 유저이메일 받아오기
            String email,
            @PathVariable("exerciseListId") Long exerciseListId
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.deleteExerciseList(email1, exerciseListId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 운동 기록 삭제하기
     */
    @DeleteMapping("/exercise-history/{exerciseHistoryId}")
    public ApiResponse<Void> deleteExerciseHistory(
            //            todo 토큰에서 유저이메일 받아오기
            String email,
            @PathVariable("exerciseHistoryId") Long exerciseHistoryId
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.deleteExerciseHistory(email1, exerciseHistoryId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 운동기록 변경하기
     */
    @PatchMapping("/exercise-history/{exerciseHistoryId}")
    public ApiResponse<Void> updateExerciseHistory(
            //            todo 토큰에서 유저이메일 받아오기
            String email,
            @PathVariable("exerciseHistoryId") Long exerciseHistoryId,
            @RequestBody UpdateExerciseHistoryRequest updateExerciseHistoryRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.updateExerciseHistory(email1, exerciseHistoryId, updateExerciseHistoryRequest);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 내 운동일지 목록 조회
     */
    @GetMapping
    public ApiResponse<UserJournalListResponse> userJournalList(
//      todo email 가져오기
            String email
    ) {
        String email1 = "dlehdwls21@naver.com";
        UserJournalListResponse userJournalList = exerciseJournalService.userJournalList(email1);
        return new ApiResponse<>(ErrorCode.SUCCESS, userJournalList);
    }

    /**
     * 내 운동일지 상세보기
     */
    @GetMapping("{journalDate}")
    public ApiResponse<UserJournalDetailResponse> userJournalDetail(
//      todo email 가져오기
            String email,
            @PathVariable("journalDate") LocalDate journalDate
    ) {
        String email1 = "dlehdwls21@naver.com";
        UserJournalDetailResponse userJournalDetail = exerciseJournalService.userJournalDetail(journalDate, email1);
        return new ApiResponse<>(ErrorCode.SUCCESS, userJournalDetail);
    }

    /**
     * 내 운동목록 휴식시간 변경
     */
    @PostMapping("/exercise-list/{exerciseListId}/rest-time")
    public ApiResponse<Void> changeExerciseListRestTime(
//            todo email가져오기
            String email,
            @PathVariable("exerciseListId") Long exerciseListId,
            @RequestBody UpdateRestTimeRequest updateRestTimeRequest
    ) {
        String email1 = "dlehdwls21@naver.com";
        exerciseJournalService.changeExerciseListRestTime(email1, exerciseListId, updateRestTimeRequest);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
