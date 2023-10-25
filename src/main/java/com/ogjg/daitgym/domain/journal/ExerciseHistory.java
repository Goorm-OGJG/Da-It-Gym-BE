package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.Exercise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    private ExerciseJournal exerciseJournal;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int setCount;

    private int weight;

    private int repetition_count;

    private boolean exercise_status;

}
