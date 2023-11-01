package com.ogjg.daitgym.journal.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ExerciseHistoryRequest {

    private Long id;
    private int setNum;
    private int weights;
    private int counts;

    /**
     * 운동목록을 처음 생성할 때
     * 생성되는 default 운동기록들을
     * 어떤 운동목록에 생성되는 운동기록인지 넣어주기 위해 사용
     */
    public ExerciseHistoryRequest putExerciseListId(Long exerciseListId) {
        this.id = exerciseListId;
        return this;
    }

}
