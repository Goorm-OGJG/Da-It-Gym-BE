package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.repository.FeedExerciseJournalCommentRepository;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalImage;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalImageRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.journal.exception.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.like.feedExerciseJournal.repository.FeedExerciseJournalLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedExerciseJournalService {

    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final FeedExerciseJournalImageRepository feedExerciseJournalImageRepository;
    private final ExerciseJournalRepository exerciseJournalRepository;
    private final FeedExerciseJournalCommentRepository feedExerciseJournalCommentRepository;
    private final FeedExerciseJournalLikeRepository feedExerciseJournalLikeRepository;

    /*
     * todo 이미지 넘어올시 이미지 저장 추가
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
     * 피드 운동일지 가져오기 목록보기 무한 스크롤
     * 분할 및 부위 분할 및 부위로 검색가능
     * 팔로우 => ?
     * 전체 => ?
     * 추천 => ?
     */

    /**
     * 피드 운동일지 상세정보 가져오기
     */

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

        feedJournal.getExerciseJournal().feedJournalDelete();
    }

    /**
     * 피드 운동일지 수
     */

    /**
     * 피드 운동일지 보관함 조회 목록보기
     */

    /**
     * 내피드 운동일지 조회
     */


    /**
     * Id로 feedJournal 검색
     */
    private FeedExerciseJournal findFeedJournalById(Long feedJournalId) {
        return feedExerciseJournalRepository.findById(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new);
    }

    /**
     * 운동일지로 피드 운동일지 찾기
     */
    public FeedExerciseJournal findFeedJournalByJournal(ExerciseJournal exerciseJournal) {
        return feedExerciseJournalRepository.findByExerciseJournal(exerciseJournal)
                .orElseThrow(NotFoundFeedJournal::new);
    }


}
