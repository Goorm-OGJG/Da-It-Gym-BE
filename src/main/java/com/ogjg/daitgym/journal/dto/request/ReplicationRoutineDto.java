package com.ogjg.daitgym.journal.dto.request;

import com.ogjg.daitgym.domain.TimeTemplate;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ReplicationRoutineDto {

    private int split;
    private TimeTemplate restTime;
    private int exerciseListNum;
    private int exerciseHistoryNum;
    private int repetitionCount;
    private int weight;
    private Long exerciseId;

    @Builder
    @QueryProjection
    public ReplicationRoutineDto(
            int split, TimeTemplate restTime, int exerciseListNum,
            int exerciseHistoryNum, int repetitionCount, int weight,
            Long exerciseId
    ) {
        this.split = split;
        this.restTime = restTime;
        this.exerciseListNum = exerciseListNum;
        this.exerciseHistoryNum = exerciseHistoryNum;
        this.repetitionCount = repetitionCount;
        this.weight = weight;
        this.exerciseId = exerciseId;
    }

    /**
     * 운동 목록 가져오기
     * @param restTime  휴식 시간
     * @param exerciseListNum  운동 순서
     * @param exerciseId  운동 Id
     */
    @QueryProjection
    public ReplicationRoutineDto(
            TimeTemplate restTime, int exerciseListNum, Long exerciseId
    ) {
        this.restTime = restTime;
        this.exerciseListNum = exerciseListNum;
        this.exerciseId = exerciseId;
    }

    /**
     * 운동기록 가져오기
     * @param exerciseHistoryNum 운동세트 번호
     * @param repetitionCount 운동횟수
     * @param weight 무게
     */
    @QueryProjection
    public ReplicationRoutineDto(
            int exerciseHistoryNum, int repetitionCount, int weight
    ) {
        this.exerciseHistoryNum = exerciseHistoryNum;
        this.repetitionCount = repetitionCount;
        this.weight = weight;
    }
}
