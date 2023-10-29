package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    private boolean journalVisibility = false;

    private boolean exerciseStatus = false;

    @NotNull
    private LocalDate journalDate = LocalDate.now();

    private TimeTemplate exerciseTime;

    private LocalDateTime exerciseStartTime;

    private LocalDateTime exerciseEndTime;

    private String split;

    public ExerciseJournal(User user) {
        this.user = user;
    }

}
