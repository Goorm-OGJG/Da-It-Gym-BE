package com.ogjg.daitgym.feed.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalCountResponse;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.service.FeedExerciseJournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 운동일지 수 가져오기
     */
    @GetMapping("/count/{nickname}")
    public ApiResponse<FeedExerciseJournalCountResponse> countExerciseJournal(
            @PathVariable("nickname") String nickname
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                feedExerciseJournalService.countExerciseJournal(nickname));
    }

    /**
     * 피드 운동일지 스크랩하기
     */
    @PostMapping("/{feedJournalId}/scrap")
    public ApiResponse<Void> feedExerciseJournalScrap(
            @PathVariable("feedJournalId") Long feedJournalId,
            //todo token 추출
            String email
    ) {
        String email1 = "dlehdwls21@naver.com";
        feedExerciseJournalService.feedExerciseJournalScrap(email1, feedJournalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 피드 목록 가져오기
     */
    @GetMapping
    public ApiResponse<List<FeedExerciseJournalListResponse>> getFeedJournalLists(
            @PageableDefault(page = 0, size = 12) Pageable pageable,
            @ModelAttribute FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        log.info(feedSearchConditionRequest.toString());
        List<FeedExerciseJournalListResponse> feedLists = feedExerciseJournalService.feedExerciseJournalLists(pageable, feedSearchConditionRequest);

        return new ApiResponse<>(ErrorCode.SUCCESS, feedLists);
    }
}
