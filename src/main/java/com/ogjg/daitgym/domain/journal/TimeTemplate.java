package com.ogjg.daitgym.domain.journal;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class TimeTemplate {

    private int hour = 0;

    @Max(59)
    private int minute = 0;

    @Max(59)
    private int second = 0;

    @Builder
    public TimeTemplate(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
}
