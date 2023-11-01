package com.ogjg.daitgym.journal.dto.response;

import com.ogjg.daitgym.journal.dto.response.dto.UserJournalListDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserJournalListResponse {
    private List<UserJournalListDto> journals = new ArrayList<>();

    public UserJournalListResponse(List<UserJournalListDto> journals) {
        this.journals = journals;
    }
}
