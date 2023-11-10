package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FeedExerciseJournalRepositoryCustom {

    Page<FeedExerciseJournal> feedExerciseJournalLists(Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

    Page<FeedExerciseJournal> feedExerciseJournalListsByFollow(String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

    Page<FeedExerciseJournal> userFeedExerciseJournalLists(String nickname, Pageable pageable);

    Page<FeedExerciseJournal> userFeedExerciseJournalCollectionLists(String nickname, Pageable pageable);

    Optional<FeedDetailResponse> feedDetail(Long feedJournalId);
}