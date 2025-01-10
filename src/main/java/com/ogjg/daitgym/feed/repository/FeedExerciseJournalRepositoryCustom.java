package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FeedExerciseJournalRepositoryCustom {

    Page<Long> feedExerciseJournalLists(Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

    Page<Long> feedExerciseJournalListsByFollow(String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

    Page<Long> userFeedExerciseJournalListsOfUser(String nickname, Pageable pageable, boolean myFeedList);

    Page<Long> userFeedExerciseJournalCollectionLists(String nickname, Pageable pageable);

    Optional<FeedDetailResponse> feedDetail(Long feedJournalId);
}