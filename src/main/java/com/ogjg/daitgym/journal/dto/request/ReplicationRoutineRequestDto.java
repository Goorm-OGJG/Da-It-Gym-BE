package com.ogjg.daitgym.journal.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class ReplicationRoutineRequestDto {

    private LocalDate journalDate;
    private Long dayId;
}
