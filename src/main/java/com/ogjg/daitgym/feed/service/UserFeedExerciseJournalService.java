package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListDto;
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
    private final FeedJournalHelper feedJournalHelper;

    /**
     * 유저 페이지 피드 운동일지 목록 가져오기
     */
    public FeedExerciseJournalListResponse userFeedExerciseJournalLists(
            String nickname, Pageable pageable
    ) {
        Page<FeedExerciseJournal> userFeedExerciseJournals =
                feedExerciseJournalRepository.userFeedExerciseJournalLists(nickname, pageable);

        List<FeedExerciseJournalListDto> content =
                feedJournalHelper.feedExerciseJournalsChangeFeedExerciseJournalsDto(userFeedExerciseJournals);

        int totalpage = userFeedExerciseJournals.getTotalPages();
        if (!content.isEmpty()) totalpage -= 1;

        return new FeedExerciseJournalListResponse(totalpage, content);
    }

    /**
     * 피드 운동일지 보관함 조회 목록보기
     */
    public FeedExerciseJournalListResponse userFeedExerciseJournalCollectionLists(
            String nickname, Pageable pageable
    ) {
        Page<FeedExerciseJournal> userFeedJournalCollections =
                feedExerciseJournalRepository.userFeedExerciseJournalCollectionLists(nickname, pageable);

        List<FeedExerciseJournalListDto> content =
                feedJournalHelper.feedExerciseJournalsChangeFeedExerciseJournalsDto(userFeedJournalCollections);

        int totalpage = userFeedJournalCollections.getTotalPages();
        if (!content.isEmpty()) totalpage -= 1;

        return new FeedExerciseJournalListResponse(totalpage, content);
    }
}
