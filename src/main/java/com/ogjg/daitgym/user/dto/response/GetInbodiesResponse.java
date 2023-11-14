package com.ogjg.daitgym.user.dto.response;

import com.ogjg.daitgym.domain.Inbody;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class GetInbodiesResponse {

    private List<InbodyDto> records;
    private List<Double> avg;

    public GetInbodiesResponse(List<Inbody> inbodies) {
        this.records = inbodies.stream()
                .map(GetInbodiesResponse.InbodyDto::new)
                .toList();

        this.avg = InbodyAvgCalculator.calculateAverages(records);
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class InbodyDto {
        private LocalDate measureAt;
        private int inbodyScore;
        private double skeletalMuscleMass;
        private double bodyFatRatio;
        private double weight;
        private int basalMetabolicRate;

        public InbodyDto(Inbody inbody) {
            this.measureAt = inbody.getMeasureAt();
            this.inbodyScore = inbody.getScore();
            this.skeletalMuscleMass = inbody.getSkeletalMuscleMass();
            this.bodyFatRatio = inbody.getBodyFatRatio();
            this.weight = inbody.getWeight();
            this.basalMetabolicRate = inbody.getBasalMetabolicRate();
        }
    }

    public static class InbodyAvgCalculator {

        public static List<Double> calculateAverages(List<InbodyDto> records) {
            InbodyDetails inbodyDetails = new InbodyDetails();

            records.stream().forEach(inbodyDetails::record);

            double count = records.size();
            return InbodyDetails.getAveragedList(count);
        }

        private static class InbodyDetails {
            static double inbodyScore = 0;
            static double basalMetabolicRate = 0;
            static double weight = 0;
            static double bodyFatRatio = 0;
            static double skeletalMuscleMass = 0;

            public void record(InbodyDto record) {
                this.inbodyScore += record.getInbodyScore();
                this.basalMetabolicRate += record.getBasalMetabolicRate();
                this.weight += record.getWeight();
                this.bodyFatRatio += record.getBodyFatRatio();
                this.skeletalMuscleMass += record.getSkeletalMuscleMass();
            }

            public static List<Double> getAveragedList(double count) {
                return List.of(inbodyScore, basalMetabolicRate, weight, bodyFatRatio, skeletalMuscleMass)
                        .stream()
                        .map((detailTotal) -> getRoundedAverage(detailTotal, count))
                        .toList();
            }

            private static double getRoundedAverage(double total, double count) {
                return Math.round(total / count * 100.0) / 100.0;
            }
        }
    }
}
