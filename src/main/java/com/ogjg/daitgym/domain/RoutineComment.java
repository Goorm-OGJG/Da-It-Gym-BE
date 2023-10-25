package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoutineComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "routine")
    private Routine routine;

    private String comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private ExerciseJournalComment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<ExerciseJournalComment> children = new ArrayList<>();
}
