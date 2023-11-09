package com.ogjg.daitgym.comment.feedExerciseJournal.controller;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.EditFeedJournalCommentRequest;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.FeedJournalCommentRequest;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.CreateFeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.EditFeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.FeedJournalChildCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.FeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.service.FeedExerciseJournalCommentService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed-journals")
public class FeedExerciseJournalCommentController {

    /**
     * 댓글 대댓글 api는 하나로 사용하며, 대댓글 작성히 parentId(댓글)를 같이 받는다
     * ex) 댓글 request : comment / 대댓글 request : comment, parentId
     */
    private final FeedExerciseJournalCommentService journalCommentService;

    /**
     * 댓글/대댓글 작성하기
     */
    @PostMapping("/{feedJournalId}/comment")
    public ApiResponse<CreateFeedJournalCommentResponse> createComment(@PathVariable Long feedJournalId,
                                                                       @RequestBody FeedJournalCommentRequest request,
                                                                       @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, journalCommentService.createComment(feedJournalId, request, oAuth2JwtUserDetails));
    }

    /**
     * 댓글/대댓글 수정하기
     */
    @PutMapping("/{feedJournalId}/comments/{commentId}")
    public ApiResponse<EditFeedJournalCommentResponse> editComment(@PathVariable Long feedJournalId,
                                                                   @PathVariable Long commentId,
                                                                   @RequestBody EditFeedJournalCommentRequest request,
                                                                   @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, journalCommentService.editComment(feedJournalId, commentId, request, oAuth2JwtUserDetails));
    }

    /**
     * 댓글/대댓글 삭제하기
     */
    @DeleteMapping(("/{feedJournalId}/comments/{commentId}"))
    public ApiResponse<Void> deleteComment(@PathVariable Long feedJournalId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        journalCommentService.deleteComment(feedJournalId, commentId, oAuth2JwtUserDetails);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 댓글 불러오기
     *
     * @Authentication LoginUser 추가하기 : 피드 운동 일지를 작성한 작성자는 본인 피드에 달린 댓글,대댓글을 모두 삭제할 수 있어야 한다. 이를 위해 작성자가 로그인 한 사람인지 권한을 확인한다.
     */
    @GetMapping("/{feedJournalId}/comments")
    public ApiResponse<FeedJournalCommentResponse> getFeedJournalComments(@PathVariable Long feedJournalId,
                                                                          @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                          @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, journalCommentService.getFeedJournalComments(feedJournalId, pageable, oAuth2JwtUserDetails));
    }

    /**
     * 대댓글 가져오기
     */
    @GetMapping("/{feedJournalId}/comments/{commentId}/child-comment")
    public ApiResponse<FeedJournalChildCommentResponse> getFeedJournalChildComment(@PathVariable Long feedJournalId,
                                                                                   @PathVariable Long commentId,
                                                                                   @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, journalCommentService.getFeedJournalChildComment(feedJournalId, commentId, oAuth2JwtUserDetails));
    }
}

