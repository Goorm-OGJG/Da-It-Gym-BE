package com.ogjg.daitgym.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class TimeTemplate {

    private int hours = 0;

    @Max(59)
    private int minutes = 0;

    @Max(59)
    private int seconds = 0;

    public TimeTemplate(TimeTemplate timeTemplate) {
        this.hours = timeTemplate.getHours();
        this.minutes = timeTemplate.getMinutes();
        this.seconds = timeTemplate.getSeconds();
    }

    public TimeTemplate(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
}
