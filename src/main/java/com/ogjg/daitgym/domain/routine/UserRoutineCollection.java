package com.ogjg.daitgym.domain.routine;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class UserRoutineCollection extends BaseEntity {

    @EmbeddedId
    private PK pk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("routine")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    public UserRoutineCollection(User user,
                                 Routine routine) {
        this.pk = new PK(user.getEmail(), routine.getId());
        this.user = user;
        this.routine = routine;
    }

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor(access = PROTECTED)
    public static class PK implements Serializable {
        private String email;
        private Long routineId;

        public PK(String email, Long routineId) {
            this.email = email;
            this.routineId = routineId;
        }
    }
}
