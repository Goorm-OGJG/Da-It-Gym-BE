package com.ogjg.daitgym.comment.feedExerciseJournal.dto.response;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.FeedCommentDto;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class FeedJournalCommentResponse {

    private int commentCnt;
    private boolean authority;
    private List<FeedCommentDto> comments;
    private int totalPage;
    private int currentPage;


    public FeedJournalCommentResponse(int commentCnt, boolean authority, Page<FeedExerciseJournalComment> feedExerciseJournalCommentList) {
        this.commentCnt = commentCnt;
        this.authority = authority;

        List<FeedExerciseJournalComment> comments = new ArrayList<>(feedExerciseJournalCommentList.getContent());
        comments.sort(Comparator.comparing(FeedExerciseJournalComment::getCreatedAt).reversed());
        this.comments = comments.stream().map(FeedCommentDto::new).collect(Collectors.toList());

        this.totalPage = feedExerciseJournalCommentList.getTotalPages();
        this.currentPage = feedExerciseJournalCommentList.getNumber();
    }
}
