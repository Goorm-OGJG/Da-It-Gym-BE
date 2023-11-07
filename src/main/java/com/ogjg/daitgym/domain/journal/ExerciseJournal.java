package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.TimeTemplate;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.journal.dto.request.ExerciseJournalCompleteRequest;
import com.ogjg.daitgym.journal.dto.request.ExerciseJournalShareRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ExerciseJournal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "journal_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    private boolean isVisible = false;

    private boolean isCompleted = false;

    @NotNull
    private LocalDate journalDate;

    private TimeTemplate exerciseTime;

    private LocalDateTime exerciseStartTime;

    private LocalDateTime exerciseEndTime;

    private String split;

    public static ExerciseJournal createJournal(
            User user, LocalDate journalDate
    ) {
        return builder()
                .user(user)
                .exerciseTime(new TimeTemplate())
                .journalDate(journalDate)
                .build();
    }

    public void journalComplete(
            ExerciseJournalCompleteRequest exerciseJournalCompleteRequest
    ) {
        this.isCompleted = exerciseJournalCompleteRequest.isCompleted();
        this.exerciseTime = exerciseJournalCompleteRequest.getExerciseTime();
    }

    public void journalShareToFeed(
            ExerciseJournalShareRequest exerciseJournalShareRequest
    ) {
        this.isVisible = exerciseJournalShareRequest.isVisible();
        this.split = exerciseJournalShareRequest.getSplit();
    }

    public void privateVisible() {
        this.isVisible = false;
    }

    @Builder
    public ExerciseJournal(
            User user, boolean isVisible, boolean isCompleted,
            LocalDate journalDate, TimeTemplate exerciseTime,
            LocalDateTime exerciseStartTime, LocalDateTime exerciseEndTime,
            String split
    ) {
        this.user = user;
        this.isVisible = isVisible;
        this.isCompleted = isCompleted;
        this.journalDate = journalDate;
        this.exerciseTime = exerciseTime;
        this.exerciseStartTime = exerciseStartTime;
        this.exerciseEndTime = exerciseEndTime;
        this.split = split;
    }
}
