package com.ogjg.daitgym.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class RegisterInbodyRequest {

    private LocalDate measureAt;
    private int inbodyScore;
    private double skeletalMuscleMass;
    private double bodyFatRatio ;
    private double weight;
    private int basalMetabolicRate;
}
