package com.ogjg.daitgym.domain.routine;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.UnitType;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "original_routine")
    private Routine originalRoutine;

    private String title;

    private String content;

    private int duration;

    private int division;

    @Enumerated(STRING)
    private UnitType unitType;

    @OneToMany(mappedBy = "routine", fetch = LAZY)
    private Set<RoutineLike> routineLikes = new HashSet<>();

    @OneToMany(mappedBy = "routine", cascade = ALL, orphanRemoval = true)
    private List<UserRoutineCollection> userRoutineCollections = new ArrayList<>();

    public int getLikesCount() {
        return routineLikes.size();
    }

    @Builder
    public Routine(User user, Routine originalRoutine, String title, String content, int duration, int division) {
        this.user = user;
        this.originalRoutine = originalRoutine;
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.division = division;
    }
}
