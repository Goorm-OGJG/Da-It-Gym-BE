package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedExerciseJournalRepositoryCustom {

    Page<FeedExerciseJournal> feedExerciseJournalLists(Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

    Page<FeedExerciseJournal> feedExerciseJournalListsByFollow(String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest);

}