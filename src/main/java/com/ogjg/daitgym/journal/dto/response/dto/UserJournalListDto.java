package com.ogjg.daitgym.journal.dto.response.dto;

import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserJournalListDto {

    private Long journalId;
    private LocalDate journalDate;

    public UserJournalListDto(ExerciseJournal exerciseJournal) {
        this.journalId = exerciseJournal.getId();
        this.journalDate = exerciseJournal.getJournalDate();
    }
}
