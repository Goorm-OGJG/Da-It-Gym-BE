package com.ogjg.daitgym.feed.controller;


import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.service.UserFeedExerciseJournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/user")
public class UserFeedExerciseJournalController {

    private final UserFeedExerciseJournalService userFeedExerciseJournalService;

    @GetMapping("{nickname}")
    public ApiResponse<FeedExerciseJournalListResponse> userFeedExerciseJournalLists(
            @PathVariable(name = "nickname") String nickname,
            @PageableDefault(page = 0, size = 12) Pageable pageable,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                userFeedExerciseJournalService.userFeedExerciseJournalLists(nickname, pageable, userDetails)
        );
    }

    @GetMapping("{nickname}/scrap")
    public ApiResponse<FeedExerciseJournalListResponse> userFeedExerciseJournalCollectionLists(
            @PathVariable(name = "nickname") String nickname,
            @PageableDefault(page = 0, size = 12) Pageable pageable
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                userFeedExerciseJournalService.userFeedExerciseJournalCollectionLists(nickname, pageable)
        );
    }

}
