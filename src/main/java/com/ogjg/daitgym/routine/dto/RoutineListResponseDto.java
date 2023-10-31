package com.ogjg.daitgym.routine.dto;

import lombok.Builder;

import java.util.List;

public class RoutineListResponseDto {
    private List<RoutineDto> routines;
    private int currentPage;
    private boolean hasNext;

    @Builder
    public RoutineListResponseDto(List<RoutineDto> routines, int currentPage, boolean hasNext) {
        this.routines = routines;
        this.currentPage = currentPage;
        this.hasNext = hasNext;
    }
}
