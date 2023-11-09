package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFeedExerciseJournalService {

    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final FeedExerciseJournalService feedExerciseJournalService;
    private final FeedJournalHelperService feedJournalHelperService;

    /**
     * 유저 페이지 피드 운동일지 목록 가져오기
     */
    public List<FeedExerciseJournalListResponse> userFeedExerciseJournalLists(
            String nickname, Pageable pageable
    ) {
        Page<FeedExerciseJournal> userFeedExerciseJournals = feedExerciseJournalRepository.userFeedExerciseJournalLists(nickname, pageable);

        return userFeedExerciseJournals.getContent()
                .stream().map(
                        feedExerciseJournal -> new FeedExerciseJournalListResponse(
                                feedExerciseJournal.getId(),
                                feedExerciseJournalService.feedExerciseJournalLikes(feedExerciseJournal.getId()),
                                feedExerciseJournalService.feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                                feedExerciseJournalService.findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                        )
                ).toList();
    }

    /**
     * 피드 운동일지 보관함 조회 목록보기
     */
    public List<FeedExerciseJournalListResponse> userFeedExerciseJournalCollectionLists(
            String nickname, Pageable pageable
    ) {
        Page<FeedExerciseJournal> userFeedJournalCollections = feedExerciseJournalRepository.userFeedExerciseJournalCollectionLists(nickname, pageable);

        return userFeedJournalCollections.getContent()
                .stream()
                .map(
                        feedExerciseJournal -> new FeedExerciseJournalListResponse(
                                feedExerciseJournal.getId(),
                                feedExerciseJournalService.feedExerciseJournalLikes(feedExerciseJournal.getId()),
                                feedExerciseJournalService.feedExerciseJournalScrapCounts(feedExerciseJournal.getId()),
                                feedExerciseJournalService.findFeedExerciseJournalImagesByFeedExerciseJournal(feedExerciseJournal).get(0).getImageUrl()
                        )
                ).toList();
    }
}
