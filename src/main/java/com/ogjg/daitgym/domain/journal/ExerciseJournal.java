package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
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

    @Enumerated(STRING)
    private JournalVisibility journalVisibility;

    @NotNull
    private LocalDate journalDate;

    private Time exerciseTime;

    private LocalDateTime exerciseStartTime;

    private LocalDateTime exerciseEndTime;

    @Enumerated(STRING)
    private Split split;

    private boolean exerciseStatus;

}
