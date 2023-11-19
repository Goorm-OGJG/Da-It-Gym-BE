package com.ogjg.daitgym.feed.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedDetailResponse;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalCountResponse;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.service.FeedExerciseJournalService;
import com.ogjg.daitgym.journal.dto.response.UserJournalDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        feedExerciseJournalService.deleteFeedJournal(userDetails.getEmail(), feedJournalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 운동일지 수 가져오기
     */
    @GetMapping("/counts/{nickname}")
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
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        feedExerciseJournalService.feedExerciseJournalScrap(userDetails.getEmail(), feedJournalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 피드 운동일지 스크랩 취소하기
     */
    @DeleteMapping("/{feedJournalId}/scrap")
    public ApiResponse<Void> feedExerciseJournalDeleteScrap(
            @PathVariable("feedJournalId") Long feedJournalId,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        feedExerciseJournalService.feedExerciseJournalDeleteScrap(userDetails.getEmail(), feedJournalId);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 피드 운동일지 목록 가져오기
     */
    @GetMapping
    public ApiResponse<FeedExerciseJournalListResponse> getFeedJournalLists(
            @PageableDefault(page = 0, size = 12) Pageable pageable,
            @ModelAttribute FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                feedExerciseJournalService.feedExerciseJournalLists(pageable, feedSearchConditionRequest)
        );
    }

    /**
     * 팔로우 피드 목록 가져오기
     */
    @GetMapping("/follow")
    public ApiResponse<FeedExerciseJournalListResponse> getFollowFeedJournalLists(
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails,
            @PageableDefault(page = 0, size = 12) Pageable pageable,
            @ModelAttribute FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                feedExerciseJournalService.followFeedJournalLists(
                        userDetails.getEmail(), pageable, feedSearchConditionRequest
                )
        );
    }

    /**
     * 피드 운동일지 상세보기
     */
    @GetMapping("{feedJournalId}/journal-detail")
    public ApiResponse<UserJournalDetailResponse> feedExerciseJournalDetail(
            @PathVariable("feedJournalId") Long journalId
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS, feedExerciseJournalService.JournalDetail(journalId)
        );
    }

    /**
     * 피드 상세보기
     */
    @GetMapping("{feedJournalId}/feed-details")
    public ApiResponse<FeedDetailResponse> feedDetail(
            @PathVariable("feedJournalId") Long feedJournalId,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                feedExerciseJournalService.feedDetail(feedJournalId, userDetails.getEmail())
        );
    }
}
