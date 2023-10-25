package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineLike extends BaseEntity {

    @EmbeddedId
    private RoutineLikePk routineLikePk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("routineId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Getter
    @Embeddable
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class RoutineLikePk implements Serializable {

        private String email;
        private Long routineId;

        public RoutineLikePk(String email, Long routineId) {
            this.email = email;
            this.routineId = routineId;
        }
    }


}
