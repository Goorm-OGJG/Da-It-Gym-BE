package com.ogjg.daitgym.journal.dto.request;

import com.ogjg.daitgym.domain.journal.TimeTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UpdateRestTimeRequest {

    private TimeTemplate restTime;

}
