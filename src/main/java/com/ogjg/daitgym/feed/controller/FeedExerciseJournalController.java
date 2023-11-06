package com.ogjg.daitgym.feed.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.feed.service.FeedExerciseJournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/journal")
public class FeedExerciseJournalController {

    private final FeedExerciseJournalService feedExerciseJournalService;

    /**
     * 운동일지 피드 삭제
     */
    @DeleteMapping("{feedJournalId}")
    public ApiResponse<Void> deleteFeedJournal(
            @PathVariable("feedJournalId") Long feedJournalId,
//            todo Token User
            String email
    ) {
        String email1 = "dlehdwls21@naver.com";
        feedExerciseJournalService.deleteFeedJournal(email1, feedJournalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

}
