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
    @JoinColumn(name = "journal_id")
    private ExerciseJournal exerciseJournal;

    @Builder
    public ExerciseJournalReplicationHistory(User user, ExerciseJournal exerciseJournal) {
        this.user = user;
        this.exerciseJournal = exerciseJournal;
    }
}
