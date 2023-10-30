package com.ogjg.daitgym.domain.routine;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class RoutineShare extends BaseEntity {

    @EmbeddedId
    private RoutineSharePK routineSharePK;

    @MapsId("routineId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @MapsId("sharedByUserEmail")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_email")
    private User sharedByUserEmail;

    @MapsId("sharedToUserEmail")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_id")
    private User sharedToUserEmail;

    private LocalDate sharedAt;

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class RoutineSharePK implements Serializable {

        private Long routineId;
        private String sharedByUserEmail;
        private String sharedToUserEmail;

        public RoutineSharePK(Long routineId, String sharedByUserEmail, String sharedToUserEmail) {
            this.routineId = routineId;
            this.sharedByUserEmail = sharedByUserEmail;
            this.sharedToUserEmail = sharedToUserEmail;
        }
    }
}
