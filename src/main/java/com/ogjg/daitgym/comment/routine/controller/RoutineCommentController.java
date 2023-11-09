package com.ogjg.daitgym.comment.routine.controller;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.EditFeedJournalCommentRequest;
import com.ogjg.daitgym.comment.routine.dto.request.RoutineCommentRequest;
import com.ogjg.daitgym.comment.routine.dto.response.CreateRoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.EditRoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.RoutineChildCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.RoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.service.RoutineCommentService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineCommentController {

    private final RoutineCommentService routineCommentService;

    /**
     * TODO @Authentication 넣기
     * 댓글 대댓글 api는 하나로 사용하며, 대댓글 작성히 parentId(댓글)를 같이 받는다
     * ex) 댓글 request : comment / 대댓글 request : comment, parentId
     */
    @PostMapping("/{routineId}/comment")
    public ApiResponse<CreateRoutineCommentResponse> createComment(@PathVariable Long routineId,
                                                                   @RequestBody RoutineCommentRequest request,
                                                                   @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineCommentService.createComment(routineId, request, oAuth2JwtUserDetails));
    }

    @PutMapping("/{routineId}/comments/{commentId}")
    public ApiResponse<EditRoutineCommentResponse> editComment(@PathVariable Long routineId,
                                                               @PathVariable Long commentId,
                                                               @RequestBody EditFeedJournalCommentRequest request,
                                                               @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineCommentService.editComment(routineId, commentId, request, oAuth2JwtUserDetails));
    }


    @DeleteMapping(("/{routineId}/comments/{commentId}"))
    public ApiResponse<Void> deleteComment(@PathVariable Long routineId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        routineCommentService.deleteComment(routineId, commentId, oAuth2JwtUserDetails);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    @GetMapping("/{routineId}/comments")
    public ApiResponse<RoutineCommentResponse> getRoutineComment(@PathVariable Long routineId,
                                                                 @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                 @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineCommentService.getRoutineComment(routineId, pageable, oAuth2JwtUserDetails));
    }

    @GetMapping("/{routineId}/comments/{commentId}/child-comment")
    public ApiResponse<RoutineChildCommentResponse> getRoutineChildComment(@PathVariable Long routineId,
                                                                           @PathVariable Long commentId,
                                                                           @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        return new ApiResponse<>(ErrorCode.SUCCESS, routineCommentService.getRoutineChildComment(routineId, commentId, oAuth2JwtUserDetails));
    }
}
