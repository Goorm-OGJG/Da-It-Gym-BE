package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.repository.FeedExerciseJournalCommentRepository;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalCollection;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalImage;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.*;
import com.ogjg.daitgym.feed.exception.AlreadyExistFeedJournalCollection;
import com.ogjg.daitgym.feed.exception.RangeOverImages;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalCollectionRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalImageRepository;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.journal.exception.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.like.feedExerciseJournal.repository.FeedExerciseJournalLikeRepository;
import com.ogjg.daitgym.s3.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final FeedExerciseJournalCollectionRepository feedExerciseJournalCollectionRepository;
    private final FeedJournalHelperService feedJournalHelperService;
    private final S3Repository s3Repository;
    @Value("${cloud.aws.default.img}")
    private String s3defaultImage;

    /*
     * 운동일지 공유시 피드에 생성
     * 이미지가 넘어오면 저장
     * */
    @Transactional
    public void shareJournalFeed(
            ExerciseJournal exerciseJournal, List<MultipartFile> imgFiles
    ) {
        FeedExerciseJournal feedExercise = feedExerciseJournalRepository.save(new FeedExerciseJournal(exerciseJournal));

        feedImagesSaveAll(feedExercise, imgFiles);
    }

    private void feedImagesSaveAll(
            FeedExerciseJournal feedExercise, List<MultipartFile> imgFiles
    ) {
        if (imgFiles == null || imgFiles.isEmpty()) {
            feedExerciseJournalImageRepository.save(new FeedExerciseJournalImage(feedExercise, s3defaultImage));
            return;
        }

        if (imgFiles.size() > 10) {
            throw new RangeOverImages();
        }

        List<FeedExerciseJournalImage> feedExerciseJournalImages = new ArrayList<>();

        imgFiles.forEach(
                multipartFile -> {
                    String uploadImageUrL = s3Repository.uploadImageToS3(multipartFile);
                    feedExerciseJournalImages.add(new FeedExerciseJournalImage(feedExercise, uploadImageUrL));
                }
        );

        feedExerciseJournalImageRepository.saveAll(feedExerciseJournalImages);
    }

    /**
     * 전체 피드 운동일지 가져오기 목록보기 무한 스크롤
     * 분할 및 부위 분할 및 부위로 검색가능
     * todo 개선하기
     */
    @Transactional(readOnly = true)
    public FeedExerciseJournalListResponse feedExerciseJournalLists(
            Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        Page<FeedExerciseJournal> feedExerciseJournals = feedExerciseJournalRepository
                .feedExerciseJournalLists(pageable, feedSearchConditionRequest);

        List<FeedExerciseJournalListDto> content = feedExerciseJournals.getContent()
                .stream()
                .map(feedExerciseJournal -> new FeedExerciseJournalListDto(
                        feedExerciseJournal.getId(),
                        feedExerciseJournalLikes(feedExerciseJournal.getId()),
                        feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                        findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                )).toList();

        int totalpage = feedExerciseJournals.getTotalPages();
        if (!content.isEmpty()) {
            totalpage -= 1;

        }

        return new FeedExerciseJournalListResponse(totalpage, content);
    }

    /**
     * 팔로우 피드 운동일지 가져오기 목록보기 무한 스크롤
     * 분할 및 부위 분할 및 부위로 검색가능
     */
    @Transactional(readOnly = true)
    public FeedExerciseJournalListResponse followFeedJournalLists(
            String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        Page<FeedExerciseJournal> feedExerciseJournals = feedExerciseJournalRepository
                .feedExerciseJournalListsByFollow(email, pageable, feedSearchConditionRequest);

        List<FeedExerciseJournalListDto> content = feedExerciseJournals.getContent()
                .stream()
                .map(feedExerciseJournal -> new FeedExerciseJournalListDto(
                        feedExerciseJournal.getId(),
                        feedExerciseJournalLikes(feedExerciseJournal.getId()),
                        feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                        findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                )).toList();

        int totalpage = feedExerciseJournals.getTotalPages();

        if (!content.isEmpty()) {
            totalpage -= 1;
        }

        return new FeedExerciseJournalListResponse(totalpage, content);
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
     * 피드 운동일지 스크랩
     */
    @Transactional
    public void feedExerciseJournalScrap(
            String email, Long feedExerciseJournalId
    ) {
        if (feedExerciseJournalCollectionScrapStatus(email,feedExerciseJournalId))
            throw new AlreadyExistFeedJournalCollection();

        feedExerciseJournalCollectionRepository.save(
                new FeedExerciseJournalCollection(
                        feedJournalHelperService.findUserByEmail(email),
                        feedJournalHelperService.findFeedJournalById(feedExerciseJournalId)
                )
        );
    }

    /**
     * 피드 운동일지 스크랩 취소
     */
    @Transactional
    public void feedExerciseJournalDeleteScrap(
            String email, Long feedExerciseJournalId
    ) {
        feedExerciseJournalCollectionRepository.delete(
                findFeedExerciseJournalCollectionByUserAndFeedExerciseJournal(email, feedExerciseJournalId)
        );
    }

    /**
     * 유저이메일과 피드 운동일지 Id를 통해 컬렉션 검색
     */
    private FeedExerciseJournalCollection findFeedExerciseJournalCollectionByUserAndFeedExerciseJournal(
            String email, Long feedExerciseJournalId
    ) {
        return feedExerciseJournalCollectionRepository.findByUserAndFeedExerciseJournal(
                feedJournalHelperService.findUserByEmail(email),
                feedJournalHelperService.findFeedJournalById(feedExerciseJournalId)
        ).orElseThrow();
    }

    /**
     * 피드에 대한 스크랩 여부
     */
    private boolean feedExerciseJournalCollectionScrapStatus(
            String email, Long feedExerciseJournalId
    ) {
        return feedExerciseJournalCollectionRepository.findByUserAndFeedExerciseJournal(
                feedJournalHelperService.findUserByEmail(email),
                feedJournalHelperService.findFeedJournalById(feedExerciseJournalId)
        ).isPresent();
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
        FeedExerciseJournal feedJournal = feedJournalHelperService.findFeedJournalById(feedJournalId);
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

            if (feedJournalImages.get(0).getImageUrl().equals(s3defaultImage))
                return;

            feedJournalImages.forEach(
                    feedExerciseJournalImage ->
                            s3Repository.deleteImageFromS3(feedExerciseJournalImage.getImageUrl())
            );

            feedExerciseJournalImageRepository.deleteAllByFeedExerciseJournal(feedJournal);
        }
    }

    /**
     * 운동일지 수 조회
     */
    @Transactional(readOnly = true)
    public FeedExerciseJournalCountResponse countExerciseJournal(String nickname) {
        User user = feedJournalHelperService.findUserByNickname(nickname);

        return new FeedExerciseJournalCountResponse(
                exerciseJournalRepository.countByUserAndIsCompleted(user, true)
        );
    }

    /**
     * todo
     * 피드 운동일지 상세정보 가져오기
     */
    public FeedDetailResponse feedDetail(
            Long feedJournalId, String email
    ) {
        FeedDetailResponse feedDetail = feedExerciseJournalRepository.feedDetail(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new);

        feedDetail.setFeedDetails(
                feedExerciseJournalLikeRepository.existsByUserEmailAndFeedExerciseJournalId(email, feedJournalId),
                feedExerciseJournalCollectionScrapStatus(email,feedJournalId),
                feedExerciseJournalLikes(feedJournalId),
                feedExerciseJournalScrapCounts(feedJournalId),
                feedImageListsDto(feedJournalHelperService.findFeedJournalById(feedJournalId))
        );

        return feedDetail;
    }

    /**
     * 피드 이미지목록 Dto로 변환
     */
    private List<FeedImageDto> feedImageListsDto(FeedExerciseJournal feedExerciseJournal) {
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

}
