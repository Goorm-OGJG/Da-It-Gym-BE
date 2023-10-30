package com.ogjg.daitgym.domain.journal;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class TimeTemplate {

    private int hour = 0;

    @Max(59)
    private int minutes = 0;

    @Max(59)
    private int seconds = 0;

    public TimeTemplate(TimeTemplate timeTemplate) {
        this.hour = timeTemplate.getHour();
        this.minutes = timeTemplate.getMinutes();
        this.seconds = timeTemplate.getSeconds();
    }
}
