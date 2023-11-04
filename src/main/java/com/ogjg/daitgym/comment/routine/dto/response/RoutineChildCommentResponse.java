package com.ogjg.daitgym.comment.routine.dto.response;

import com.ogjg.daitgym.comment.routine.dto.RoutineChildDto;
import com.ogjg.daitgym.domain.RoutineComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RoutineChildCommentResponse {
    private int childCommentsCnt;
    private boolean authority;
    private List<RoutineChildDto> childComments;


    public RoutineChildCommentResponse(int childCommentsCnt, boolean authority, List<RoutineComment> routineComments) {
        this.childCommentsCnt = childCommentsCnt;
        this.authority = authority;

        List<RoutineComment> sortedComments = new ArrayList<>(routineComments);
        sortedComments.sort(Comparator.comparing(RoutineComment::getCreatedAt).reversed());
        this.childComments = sortedComments.stream().map(RoutineChildDto::new).collect(Collectors.toList());
    }
}
