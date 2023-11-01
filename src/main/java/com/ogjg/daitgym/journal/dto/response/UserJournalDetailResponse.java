package com.ogjg.daitgym.journal.dto.response;

import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserJournalDetailResponse {

    private UserJournalDetailDto journal;

    public UserJournalDetailResponse(UserJournalDetailDto journal) {
        this.journal = journal;
    }
}
