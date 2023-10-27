package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import jakarta.persistence.*;
import lombok.Builder;
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
    private RoutineComment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<RoutineComment> children = new ArrayList<>();

    public void updateCommentParent(RoutineComment parentComment) {
        this.parent = parentComment;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
    @Builder
    public RoutineComment(User user, Routine routine, String comment) {
        this.user = user;
        this.routine = routine;
        this.comment = comment;
    }
}
