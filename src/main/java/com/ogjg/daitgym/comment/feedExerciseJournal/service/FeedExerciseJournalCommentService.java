package com.ogjg.daitgym.comment.feedExerciseJournal.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.EditFeedJournalCommentRequest;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.request.FeedJournalCommentRequest;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.CreateFeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.EditFeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.FeedJournalChildCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.dto.response.FeedJournalCommentResponse;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournalComment;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundUser;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.WrongApproach;
import com.ogjg.daitgym.comment.feedExerciseJournal.repository.FeedExerciseJournalCommentRepository;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedExerciseJournalCommentService {

    private final FeedExerciseJournalRepository feedJournalRepository;
    private final UserRepository userRepository;
    private final FeedExerciseJournalCommentRepository feedJournalCommentRepository;

    /**
     * 댓글/대댓글 작성하기
     */
    @Transactional
    public CreateFeedJournalCommentResponse createComment(Long feedJournalId,
                                                          FeedJournalCommentRequest request,
                                                          OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        FeedExerciseJournal feedJournal = feedJournalRepository.findById(feedJournalId).orElseThrow(NotFoundFeedJournal::new);
        Long parentId = request.getParentId();

        FeedExerciseJournalComment feedJournalComment = FeedExerciseJournalComment.builder()
                .comment(request.getComment())
                .feedExerciseJournal(feedJournal)
                .user(user)
                .build();

        if (parentId != null) {
            FeedExerciseJournalComment parentComment;
            parentComment = feedJournalCommentRepository.findById(parentId).orElseThrow(NotFoundFeedJournalComment::new);
            feedJournalComment.updateCommentParent(parentComment);
        }
        feedJournalCommentRepository.save(feedJournalComment);

        return CreateFeedJournalCommentResponse.builder()
                .parentId(request.getParentId())
                .id(feedJournalComment.getId())
                .comment(request.getComment())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .build();
    }

    @Transactional
    public EditFeedJournalCommentResponse editComment(Long feedJournalId,
                                                      Long commentId,
                                                      EditFeedJournalCommentRequest request,
                                                      OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        FeedExerciseJournalComment journalComment = feedJournalCommentRepository.findByFeedExerciseJournalIdAndId(feedJournalId, commentId).orElseThrow(NotFoundFeedJournalComment::new);

        if (!Objects.equals(user.getNickname(), journalComment.getUser().getNickname())) {
            throw new WrongApproach("작성한 사용자만 수정할 수 있습니다");
        }

        journalComment.updateComment(request.getComment());
        Long parentId = (journalComment.getParent() != null) ? journalComment.getParent().getId() : null;

        return EditFeedJournalCommentResponse.builder()
                .parentId(parentId)
                .id(commentId)
                .comment(request.getComment())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .build();
    }

    @Transactional
    public void deleteComment(Long feedJournalId,
                              Long commentId,
                              OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        ExerciseJournal exerciseJournal = getExerciseJournal(feedJournalId);

        FeedExerciseJournalComment journalComment = feedJournalCommentRepository.findByFeedExerciseJournalIdAndId(feedJournalId, commentId).orElseThrow(NotFoundFeedJournalComment::new);
        String userEmail = user.getEmail();
        String exerciseJournalWriter = exerciseJournal.getUser().getEmail();
        String feedCommentWriter = journalComment.getUser().getEmail();

        if (!Objects.equals(userEmail, feedCommentWriter) && !Objects.equals(userEmail, exerciseJournalWriter)) {
            throw new WrongApproach("작성한 사용자만 삭제할 수 있습니다");
        }
        feedJournalCommentRepository.delete(journalComment);
    }


    /**
     * 피드운동일지에 대한 댓글 가져오기
     * authority : 로그인한 유저와 피드운동일지를 작성한 유저가 같은지 확인
     * 페이징 처리 : Pageable
     * 최신순으로 댓글 정렬 : FeedJournalCommentResponse 안에서 처
     */
    @Transactional(readOnly = true)
    public FeedJournalCommentResponse getFeedJournalComments(Long feedJournalId,
                                                             Pageable pageable,
                                                             OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        ExerciseJournal exerciseJournal = getExerciseJournal(feedJournalId);
        boolean authority = checkAuthority(user, exerciseJournal);

        int commentCount = feedJournalCommentRepository.countByFeedExerciseJournalIdAndParentIdIsNull(feedJournalId);

        Page<FeedExerciseJournalComment> feedComments = feedJournalCommentRepository.findByFeedExerciseJournalIdAndParentIdIsNullOrderByCreatedAtDesc(feedJournalId, pageable);
        return new FeedJournalCommentResponse(commentCount, authority, feedComments);
    }

    /**
     * 피드운동일지에 대한 대댓글 가져오기
     * authority : 로그인한 유저와 루틴을 작성한 유저가 같은지 확인
     * 최신순으로 댓글 정렬 : FeedJournalChildCommentResponse 안에서 처리
     */
    @Transactional(readOnly = true)
    public FeedJournalChildCommentResponse getFeedJournalChildComment(Long feedJournalId,
                                                                      Long commentId,
                                                                      OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        User user = getUserByEmail(oAuth2JwtUserDetails.getEmail());
        ExerciseJournal exerciseJournal = getExerciseJournal(feedJournalId);
        boolean authority = checkAuthority(user, exerciseJournal);

        int childCommentsCnt = feedJournalCommentRepository.countByFeedExerciseJournalIdAndParentIdIsNotNull(feedJournalId);
        List<FeedExerciseJournalComment> feedJournalChildComments = feedJournalCommentRepository.findByFeedExerciseJournalIdAndParentIdOrderByCreatedAtDesc(feedJournalId, commentId);

        return new FeedJournalChildCommentResponse(childCommentsCnt, authority, feedJournalChildComments);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }

    private ExerciseJournal getExerciseJournal(Long feedJournalId) {
        FeedExerciseJournal feedExerciseJournal = feedJournalRepository.findById(feedJournalId).orElseThrow(NotFoundFeedJournal::new);
        return feedExerciseJournal.getExerciseJournal();
    }

    private boolean checkAuthority(User user, ExerciseJournal exerciseJournal) {
        return Objects.equals(user.getEmail(), exerciseJournal.getUser().getEmail());
    }
}