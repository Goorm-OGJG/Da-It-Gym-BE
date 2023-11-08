package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.repository.FeedExerciseJournalCommentRepository;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalCollection;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalImage;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalCountResponse;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalCollectionRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalImageRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.journal.exception.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.like.feedExerciseJournal.repository.FeedExerciseJournalLikeRepository;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedExerciseJournalService {

    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final FeedExerciseJournalImageRepository feedExerciseJournalImageRepository;
    private final ExerciseJournalRepository exerciseJournalRepository;
    private final FeedExerciseJournalCommentRepository feedExerciseJournalCommentRepository;
    private final FeedExerciseJournalLikeRepository feedExerciseJournalLikeRepository;
    private final FeedExerciseJournalCollectionRepository feedExerciseJournalCollectionRepository;
    private final UserRepository userRepository;

    /*
     * todo 이미지 넘어올시 이미지 저장 추가
     * todo 이미지 null 이면 기본 이미지 저장
     * 운동일지 공유시 피드에 생성
     * 이미지가 넘어오면 저장
     * */
    @Transactional
    public void shareJournalFeed(
            ExerciseJournal exerciseJournal, List<MultipartFile> imgFiles
    ) {
        FeedExerciseJournal feedExercise = feedExerciseJournalRepository.save(new FeedExerciseJournal(exerciseJournal));

        if (!imgFiles.isEmpty()) {
            imgFiles.forEach(
                    multipartFile -> feedExerciseJournalImageRepository.save(
                            //todo s3에 저장 후 이미지경로 입력하기
                            new FeedExerciseJournalImage(feedExercise, "s3 이미지 파일 경로")
                    )
            );
        }
    }

    /**
     * 전체 피드 운동일지 가져오기 목록보기 무한 스크롤
     * 분할 및 부위 분할 및 부위로 검색가능
     * todo 개선하기
     */
    public List<FeedExerciseJournalListResponse> feedExerciseJournalLists(
            Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        Page<FeedExerciseJournal> feedExerciseJournals = feedExerciseJournalRepository
                .feedExerciseJournalLists(pageable, feedSearchConditionRequest);

        return feedExerciseJournals.getContent()
                .stream()
                .map(feedExerciseJournal -> new FeedExerciseJournalListResponse(
                        feedExerciseJournal.getId(),
                        feedExerciseJournalLikes(feedExerciseJournal.getId()),
                        feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                        findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                )).collect(Collectors.toList());
    }

    /**
     * 팔로우 피드 운동일지 가져오기 목록보기 무한 스크롤
     * 분할 및 부위 분할 및 부위로 검색가능
     */
    public List<FeedExerciseJournalListResponse> followFeedJournalLists(
            String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        Page<FeedExerciseJournal> feedExerciseJournals = feedExerciseJournalRepository
                .feedExerciseJournalListsByFollow(email, pageable, feedSearchConditionRequest);

        return feedExerciseJournals.getContent().stream()
                .map(feedExerciseJournal -> new FeedExerciseJournalListResponse(
                        feedExerciseJournal.getId(),
                        feedExerciseJournalLikes(feedExerciseJournal.getId()),
                        feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                        findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                )).collect(Collectors.toList());
    }

    /**
     * 피드 운동일지 좋아요 수
     */
    private int feedExerciseJournalLikes(Long feedJournalId) {
        return feedExerciseJournalLikeRepository.countByFeedJournalLikePkFeedExerciseJournalId(feedJournalId);
    }

    /**
     * 피드 운동일지 스크랩 횟수
     */
    private int feedExerciseJournalScrapCounts(Long feedExerciseJournalId) {
        return feedExerciseJournalCollectionRepository.countByPkFeedExerciseJournalId(feedExerciseJournalId);
    }

    /**
     * 피드운동일지 이미지 가져오기
     */
    private List<FeedExerciseJournalImage> findFeedExerciseJournalImagesByFeedExerciseJournal(
            FeedExerciseJournal feedExerciseJournal
    ) {
        return feedExerciseJournalImageRepository.findAllByFeedExerciseJournal(feedExerciseJournal);
    }


    /**
     * 피드 운동일지 상세정보 가져오기
     */

    /**
     * 피드 운동일지 보관함 조회 목록보기
     */

    /**
     * 내 피드 운동일지 조회
     */


    /**
     * 피드 운동일지 스크랩
     */
    public void feedExerciseJournalScrap(
            String email, Long feedExerciseJournalId
    ) {
        feedExerciseJournalCollectionRepository.save(
                new FeedExerciseJournalCollection(
                        findUserByEmail(email),
                        findFeedJournalById(feedExerciseJournalId)
                )
        );
    }

    /**
     * 피드 운동일지 삭제하기
     * 피드 좋아요 삭제
     * 피드 댓글 삭제
     * 피드 이미지 삭제
     * 운동일지 공개여부 false로 변경
     */
    @Transactional
    public void deleteFeedJournal(
            String email, Long feedJournalId
    ) {
        FeedExerciseJournal feedJournal = findFeedJournalById(feedJournalId);
        feedExerciseJournalCommentRepository.deleteAllByFeedExerciseJournal(feedJournal);
        feedExerciseJournalLikeRepository.deleteAllByFeedExerciseJournal(feedJournal);
        feedExerciseJournalImageRepository.deleteAllByFeedExerciseJournal(feedJournal);

        if (!feedJournal.getExerciseJournal().getUser().getEmail().equals(email))
            throw new UserNotAuthorizedForJournal();

        feedExerciseJournalRepository.delete(feedJournal);

        feedJournal.getExerciseJournal().privateVisible();
    }

    /**
     * 운동일지 수 조회
     */
    @Transactional(readOnly = true)
    public FeedExerciseJournalCountResponse countExerciseJournal(String nickname) {
        User user = findUserByNickname(nickname);

        return new FeedExerciseJournalCountResponse(
                exerciseJournalRepository.countByUserAndIsCompleted(user, true)
        );
    }

    /**
     * Id로 feedJournal 검색
     */
    private FeedExerciseJournal findFeedJournalById(Long feedJournalId) {
        return feedExerciseJournalRepository.findById(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }

    /**
     * 운동일지로 피드 운동일지 찾기
     */
    public FeedExerciseJournal findFeedJournalByJournal(ExerciseJournal exerciseJournal) {
        return feedExerciseJournalRepository.findByExerciseJournal(exerciseJournal)
                .orElseThrow(NotFoundFeedJournal::new);
    }


}
