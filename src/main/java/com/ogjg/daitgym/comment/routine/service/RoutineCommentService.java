package com.ogjg.daitgym.comment.routine.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.EditFeedJournalCommentRequest;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundUser;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.comment.routine.dto.request.RoutineCommentRequest;
import com.ogjg.daitgym.comment.routine.dto.response.CreateRoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.EditRoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.RoutineChildCommentResponse;
import com.ogjg.daitgym.comment.routine.dto.response.RoutineCommentResponse;
import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutineComment;
import com.ogjg.daitgym.comment.routine.repository.RoutineCommentRepository;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.domain.routine.RoutineComment;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoutineCommentService {
    private final RoutineCommentRepository routineCommentRepository;
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    /**
     * 루틴에 대한 댓글/대댓글 작성
     */
    @Transactional
    public CreateRoutineCommentResponse createComment(Long routineId,
                                                      RoutineCommentRequest request,
                                                      OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        Routine routine = getRoutine(routineId);
        Long parentId = request.getParentId();

        RoutineComment routineComment = RoutineComment.builder()
                .user(user)
                .routine(routine)
                .comment(request.getComment())
                .build();

        if (parentId != null) {
            RoutineComment parentComment;
            parentComment = routineCommentRepository.findById(parentId).orElseThrow(NotFoundRoutineComment::new);
            routineComment.updateCommentParent(parentComment);
        }
        routineCommentRepository.save(routineComment);

        return CreateRoutineCommentResponse.builder()
                .parentId(request.getParentId())
                .id(routineComment.getId())
                .comment(request.getComment())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .build();
    }

    /**
     * 댓글/대댓글 수정
     */
    @Transactional
    public EditRoutineCommentResponse editComment(Long routineId,
                                                  Long commentId,
                                                  EditFeedJournalCommentRequest request,
                                                  OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());

        RoutineComment routineComment = routineCommentRepository.findByRoutineIdAndId(routineId, commentId).orElseThrow(NotFoundRoutineComment::new);

        if (!Objects.equals(user.getNickname(), routineComment.getUser().getNickname())) {
            throw new WrongApproach("작성한 사용자만 수정할 수 있습니다");
        }

        routineComment.updateComment(request.getComment());
        Long parentId = (routineComment.getParent() != null) ? routineComment.getParent().getId() : null;


        return EditRoutineCommentResponse.builder()
                .parentId(parentId)
                .id(routineComment.getId())
                .comment(request.getComment())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .build();

    }


    /**
     * 댓글/대댓글 식제
     */
    @Transactional
    public void deleteComment(Long routineId,
                              Long commentId,
                              OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        RoutineComment routineComment = routineCommentRepository.findByRoutineIdAndId(routineId, commentId).orElseThrow(NotFoundRoutineComment::new);

        String routineWriter = routineComment.getRoutine().getUser().getEmail();
        String routineCommentWriter = routineComment.getUser().getEmail();
        String userEmail = user.getEmail();

        if (!Objects.equals(userEmail, routineCommentWriter) && !Objects.equals(userEmail, routineWriter)) {
            throw new WrongApproach("작성한 사용자만 삭제할 수 있습니다");
        }
        routineCommentRepository.delete(routineComment);

    }


    /**
     * 루틴에 대한 댓글 가져오기
     * authority : 로그인한 유저와 루틴을 작성한 유저가 같은지 확인
     * 페이징 처리 : Pageable
     * 최신순으로 댓글 정렬 : RoutineCommentResponse 안에서 처리
     */

    @Transactional(readOnly = true)
    public RoutineCommentResponse getRoutineComment(Long routineId,
                                                    Pageable pageable,
                                                    OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        Routine routine = getRoutine(routineId);
        boolean authority = checkAuthority(user, routine);
        int commentCount = routineCommentRepository.countByRoutineIdAndParentIdIsNull(routineId);

        Page<RoutineComment> routineComments = routineCommentRepository.findByRoutineIdAndParentIdIsNullOrderByCreatedAtDesc(routineId, pageable);
        return new RoutineCommentResponse(commentCount, authority, routineComments);
    }

    /**
     * 루틴에 대한 대댓글 가져오기
     * authority : 로그인한 유저와 루틴을 작성한 유저가 같은지 확인
     * 최신순으로 댓글 정렬 : RoutineChildCommentResponse 안에서 처리
     */
    @Transactional(readOnly = true)
    public RoutineChildCommentResponse getRoutineChildComment(Long routineId,
                                                              Long commentId,
                                                              OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        Routine routine = getRoutine(routineId);
        boolean authority = checkAuthority(user, routine);
        int childCommentsCnt = routineCommentRepository.countByRoutineIdAndParentIdIsNotNull(routineId);
        List<RoutineComment> routineComments = routineCommentRepository.findByRoutineIdAndParentIdOrderByCreatedAtDesc(routineId, commentId);
        return new RoutineChildCommentResponse(childCommentsCnt, authority, routineComments);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }

    private Routine getRoutine(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(NotFoundRoutine::new);
    }

    private boolean checkAuthority(User user, Routine routine) {
        return Objects.equals(user.getEmail(), routine.getUser().getEmail());
    }
}

