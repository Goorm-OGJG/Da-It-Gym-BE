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
}
