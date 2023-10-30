package com.ogjg.daitgym.journal.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateJournalRequest {

    private LocalDate journalDate;
}
