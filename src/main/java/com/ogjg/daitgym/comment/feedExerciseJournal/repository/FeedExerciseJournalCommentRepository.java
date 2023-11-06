package com.ogjg.daitgym.comment.feedExerciseJournal.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FeedExerciseJournalCommentRepository extends JpaRepository<FeedExerciseJournalComment, Long> {

    Page<FeedExerciseJournalComment> findByFeedExerciseJournalIdAndParentIdIsNull(Long feedId, Pageable pageable);

    List<FeedExerciseJournalComment> findByFeedExerciseJournalIdAndParentId(Long feedId, Long commentId);

    Optional<FeedExerciseJournalComment> findByFeedExerciseJournalIdAndId(Long feedJournalId, Long commentId);

    int countByFeedExerciseJournalIdAndParentIdIsNull(Long feedJournalId);

    int countByFeedExerciseJournalIdAndParentIdIsNotNull(Long feedJournalId);

    void deleteAllByFeedExerciseJournal(FeedExerciseJournal feedExerciseJournal);
}
