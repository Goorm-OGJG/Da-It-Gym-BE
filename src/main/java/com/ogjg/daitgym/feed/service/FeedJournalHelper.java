package com.ogjg.daitgym.feed.service;


import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.repository.FeedExerciseJournalCommentRepository;
import com.ogjg.daitgym.common.exception.feed.NotFoundFeedJournalCollection;
import com.ogjg.daitgym.common.exception.feed.RangeOverImages;
import com.ogjg.daitgym.common.exception.journal.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalCollection;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalImage;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListDto;
import com.ogjg.daitgym.feed.dto.response.FeedImageDto;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalCollectionRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalImageRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.journal.service.ExerciseJournalHelper;
import com.ogjg.daitgym.like.feedExerciseJournal.repository.FeedExerciseJournalLikeRepository;
import com.ogjg.daitgym.s3.repository.S3Repository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
//annotation으로 만들어서 쓸수있을수있다
public class FeedJournalHelper {

    @Value("${cloud.aws.default.img}")
    private String s3defaultImage;
    private final S3Repository s3Repository;
    private final UserRepository userRepository;
    private final ExerciseJournalHelper exerciseJournalHelper;
    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final FeedExerciseJournalLikeRepository feedExerciseJournalLikeRepository;
    private final FeedExerciseJournalImageRepository feedExerciseJournalImageRepository;
    private final FeedExerciseJournalCommentRepository feedExerciseJournalCommentRepository;
    private final FeedExerciseJournalCollectionRepository feedExerciseJournalCollectionRepository;

    /**
     * Id로 feedJournal 검색
     */
    public FeedExerciseJournal findFeedJournalById(Long feedJournalId) {
        return feedExerciseJournalRepository.findById(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new);
    }

    /**
     * FeedId로 JournalId 검색
     */
    public ExerciseJournal findExerciseJournalByFeedJournalId(Long feedJournalId) {
        return feedExerciseJournalRepository.findById(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new)
                .getExerciseJournal();
    }

    /**
     * 운동일지로 피드운동일지가 존재하는지 확인하기
     */
    public boolean checkExistFeedExerciseJournalByExerciseJournal(ExerciseJournal exerciseJournal) {
        return feedExerciseJournalRepository.findByExerciseJournal(exerciseJournal).isPresent();
    }

    /**
     * todo
     */
    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    /**
     * todo
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }

    /**
     * 운동일지 공유시 피드에 생성
     * 이미지가 넘어오면 저장 안넘어오면 default 이미지
     */
    public void shareJournalFeed(
            ExerciseJournal exerciseJournal, List<MultipartFile> imgFiles
    ) {
        FeedExerciseJournal feedExercise =
                feedExerciseJournalRepository.save(new FeedExerciseJournal(exerciseJournal));

        feedImagesSaveAll(feedExercise, imgFiles);
    }

    /**
     * 이미지가 넘어오면 저장 안넘어오면 default 이미지
     * 최대 이미지수 10장
     */
    private void feedImagesSaveAll(
            FeedExerciseJournal feedExercise, List<MultipartFile> imgFiles
    ) {
        if (imgFiles == null || imgFiles.isEmpty()) {
            feedExerciseJournalImageRepository.save(new FeedExerciseJournalImage(feedExercise, s3defaultImage));
            return;
        }

        if (imgFiles.size() > 10) throw new RangeOverImages();

        List<FeedExerciseJournalImage> feedExerciseJournalImages = new ArrayList<>();

        imgFiles.forEach(multipartFile -> {
            String uploadImageUrL = s3Repository.uploadImageToS3(multipartFile);
            feedExerciseJournalImages.add(new FeedExerciseJournalImage(feedExercise, uploadImageUrL));
        });

        feedExerciseJournalImageRepository.saveAll(feedExerciseJournalImages);
    }


    /**
     * 피드 이미지목록 Dto로 변환
     */
    public List<FeedImageDto> feedImageListsDto(FeedExerciseJournal feedExerciseJournal) {
        return findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal)
                .stream()
                .map(feedExerciseJournalImage -> new FeedImageDto(
                        feedExerciseJournalImage.getId(), feedExerciseJournalImage.getImageUrl())
                )
                .toList();
    }

    /**
     * 운동일지로 피드 운동일지 찾기
     */
    public FeedExerciseJournal findFeedJournalByJournal(ExerciseJournal exerciseJournal) {
        return feedExerciseJournalRepository.findByExerciseJournal(exerciseJournal)
                .orElseThrow(NotFoundFeedJournal::new);
    }

    /**
     * 피드 운동일지 상세보기를 Dto로 변환
     */
    public List<FeedExerciseJournalListDto> feedExerciseJournalsChangeFeedExerciseJournalsDto(
            Page<Long> feedExerciseJournalsId
    ) {
        return feedExerciseJournalsId.getContent()
                .stream()
                .map(feedExerciseJournalId -> new FeedExerciseJournalListDto(
                        feedExerciseJournalId,
                        feedExerciseJournalLikes(feedExerciseJournalId),
                        feedExerciseJournalScrapCounts(feedExerciseJournalId),
                        feedCoverImage(findFeedExerciseJournalImagesByFeedExerciseJournal(
                                feedExerciseJournalRepository.findById(feedExerciseJournalId).orElseThrow(NotFoundFeedJournal::new)
                        ))
                )).toList();
    }

    /**
     * 유저이메일과 피드 운동일지 Id를 통해 컬렉션 검색
     */
    public FeedExerciseJournalCollection findFeedExerciseJournalCollectionByUserAndFeedExerciseJournal(
            String email, Long feedExerciseJournalId
    ) {
        return feedExerciseJournalCollectionRepository.findByUserAndFeedExerciseJournal(
                findUserByEmail(email),
                findFeedJournalById(feedExerciseJournalId)
        ).orElseThrow(NotFoundFeedJournalCollection::new);
    }

    /**
     * 피드에 대한 스크랩 여부
     */
    public boolean feedExerciseJournalCollectionScrapStatus(
            String email, Long feedExerciseJournalId
    ) {
        return feedExerciseJournalCollectionRepository.findByUserAndFeedExerciseJournal(
                findUserByEmail(email),
                findFeedJournalById(feedExerciseJournalId)
        ).isPresent();
    }

    /**
     * 피드 운동일지 좋아요 수
     */
    public int feedExerciseJournalLikes(Long feedJournalId) {
        return feedExerciseJournalLikeRepository.countByFeedJournalLikePkFeedExerciseJournalId(feedJournalId);
    }

    /**
     * 피드 운동일지 스크랩 횟수
     */
    public int feedExerciseJournalScrapCounts(Long feedExerciseJournalId) {
        return feedExerciseJournalCollectionRepository.countByPkFeedExerciseJournalId(feedExerciseJournalId);
    }

    /**
     * 피드운동일지 이미지 가져오기
     */
    public List<FeedExerciseJournalImage> findFeedExerciseJournalImagesByFeedExerciseJournal(
            FeedExerciseJournal feedExerciseJournal
    ) {
        return feedExerciseJournalImageRepository.findAllByFeedExerciseJournal(feedExerciseJournal);
    }

    /**
     * 피드 운동일지 삭제하기
     * 피드 좋아요 삭제
     * 피드 댓글 삭제
     * 피드 이미지 삭제
     * 운동일지 공개여부 false로 변경
     */
    public void deleteFeedJournal(
            String email, Long feedJournalId
    ) {
        FeedExerciseJournal feedJournal = findFeedJournalById(feedJournalId);
        exerciseJournalHelper.isAuthorizedForJournal(email, feedJournal.getExerciseJournal().getId());
        feedExerciseJournalCommentRepository.deleteAllByFeedExerciseJournal(feedJournal);
        feedExerciseJournalLikeRepository.deleteAllByFeedExerciseJournal(feedJournal);
        feedImagesDelete(feedJournal);
        feedExerciseJournalCollectionRepository.deleteAllByFeedExerciseJournal(feedJournal);

        if (!feedJournal.getExerciseJournal().getUser().getEmail().equals(email))
            throw new UserNotAuthorizedForJournal();

        feedExerciseJournalRepository.delete(feedJournal);

        feedJournal.getExerciseJournal().changeToPrivate();
    }

    /**
     * 피드 운동일지 이미지 삭제하기
     * s3 업로드된 이미지 삭제하고 db에서도 삭제
     */
    private void feedImagesDelete(
            FeedExerciseJournal feedJournal
    ) {
        List<FeedExerciseJournalImage> feedJournalImages = findFeedExerciseJournalImagesByFeedExerciseJournal(feedJournal);

        if (feedJournalImages != null && !feedJournalImages.isEmpty()) {

            feedJournalImages.stream()
                    .filter(img -> !img.getImageUrl().equals(s3defaultImage))
                    .forEach(feedExerciseJournalImage ->
                            s3Repository.deleteImageFromS3(feedExerciseJournalImage.getImageUrl()));

            feedExerciseJournalImageRepository.deleteAllByFeedExerciseJournal(feedJournal);
        }
    }

    public String feedCoverImage(List<FeedExerciseJournalImage> feedJournalImages) {
        return feedJournalImages.get(0).getImageUrl();
    }
}
