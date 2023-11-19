package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseJournalReplicationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_journal_replication_history")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "original_journal_id")
    private ExerciseJournal originalExerciseJournal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "replicated_journal_id")
    private ExerciseJournal replicatedExerciseJournal;


    @Builder
    public ExerciseJournalReplicationHistory(
            User user, ExerciseJournal originalExerciseJournal,
            ExerciseJournal replicatedExerciseJournal
    ) {
        this.user = user;
        this.originalExerciseJournal = originalExerciseJournal;
        this.replicatedExerciseJournal = replicatedExerciseJournal;
    }
}
