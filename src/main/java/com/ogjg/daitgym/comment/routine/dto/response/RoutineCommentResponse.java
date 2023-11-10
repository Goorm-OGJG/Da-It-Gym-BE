package com.ogjg.daitgym.comment.routine.dto.response;

import com.ogjg.daitgym.comment.routine.dto.RoutineCommentDto;
import com.ogjg.daitgym.domain.routine.RoutineComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RoutineCommentResponse {
    private int commentCnt;
    private boolean authority;
    private List<RoutineCommentDto> comments;
    private int totalPage;
    private int currentPage;
    public RoutineCommentResponse(int commentCnt, boolean authority, Page<RoutineComment> routineComments) {
        this.commentCnt = commentCnt;
        this.authority = authority;
        List<RoutineComment> comments = new ArrayList<>(routineComments.getContent());
        comments.sort(Comparator.comparing(RoutineComment::getCreatedAt).reversed());
        this.comments = comments.stream().map(RoutineCommentDto::new).collect(Collectors.toList());
        this.totalPage = routineComments.getTotalPages();
        this.currentPage = routineComments.getNumber();
    }
}
