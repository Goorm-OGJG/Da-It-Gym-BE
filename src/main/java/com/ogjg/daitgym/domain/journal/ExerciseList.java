package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.Exercise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_list_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "journal_id")
    private ExerciseJournal exerciseJournal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Time restTime;

}
