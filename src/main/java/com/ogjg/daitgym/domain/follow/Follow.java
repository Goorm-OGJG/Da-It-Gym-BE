package com.ogjg.daitgym.domain.follow;

import com.ogjg.daitgym.domain.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Follow {

    @EmbeddedId
    private PK followPK;

    @MapsId("targetEmail")
    @ManyToOne(fetch = LAZY)
    private User target;

    @MapsId("followerEmail")
    @ManyToOne(fetch = LAZY)
    private User follower;

    public Follow(PK followPK, User target, User user) {
        this.followPK = followPK;
        this.target = target;
        this.follower = user;
    }

    public static PK createFollowPK(String targetEmail, String followerEmail) {
        return new PK(targetEmail, followerEmail);
    }

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class PK implements Serializable {

        private String targetEmail;
        private String followerEmail;

        public PK(String targetEmail, String followerEmail) {
            this.targetEmail = targetEmail;
            this.followerEmail = followerEmail;
        }
    }
}