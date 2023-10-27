package com.ogjg.daitgym.domain.feed;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feed_journal_id")
    private FeedExerciseJournal feedExerciseJournal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private FeedExerciseJournalComment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<FeedExerciseJournalComment> children = new ArrayList<>();


    public void updateCommentParent(FeedExerciseJournalComment parentComment) {
        this.parent = parentComment;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    /**
     * TODO user 추가하기
     *
     * @param comment
     * @param feedExerciseJournal
     */
    @Builder
    public FeedExerciseJournalComment(String comment, User user, FeedExerciseJournal feedExerciseJournal) {
        this.comment = comment;
        this.user = user;
        this.feedExerciseJournal = feedExerciseJournal;
    }

}
