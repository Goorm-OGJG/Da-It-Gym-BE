package com.ogjg.daitgym.journal.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class JournalRequest {

    private LocalDate journalDate;
    private List<ExerciseListRequest> exercises = new ArrayList<>();

}
