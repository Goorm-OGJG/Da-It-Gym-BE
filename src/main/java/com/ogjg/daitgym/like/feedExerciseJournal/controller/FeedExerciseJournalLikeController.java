package com.ogjg.daitgym.like.feedExerciseJournal.controller;

import com.ogjg.daitgym.like.feedExerciseJournal.dto.FeedExerciseJournalLikeResponse;
import com.ogjg.daitgym.like.feedExerciseJournal.service.FeedExerciseJournalLikeService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed-journals/{feedJournalId}/like")
@RequiredArgsConstructor
public class FeedExerciseJournalLikeController {

    private final FeedExerciseJournalLikeService feedJournalLikeService;

    /**
     * TODO User 추가하기
     */

    @PostMapping
    public ApiResponse<FeedExerciseJournalLikeResponse> feedJournalLike(@PathVariable Long feedJournalId) {
        return new ApiResponse<>(ErrorCode.SUCCESS, feedJournalLikeService.feedJournalLike(feedJournalId));
    }

    @DeleteMapping
    public ApiResponse<FeedExerciseJournalLikeResponse> feedJournalUnLike(@PathVariable Long feedJournalId) {
        return new ApiResponse<>(ErrorCode.SUCCESS, feedJournalLikeService.feedJournalUnLike(feedJournalId));
    }
}
