package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Builder;
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
    private FollowPK followPK;

    @MapsId("targetEmail")
    @ManyToOne(fetch = LAZY)
    private User target;

    @MapsId("followerEmail")
    @ManyToOne(fetch = LAZY)
    private User follower;

    @Builder
    public Follow(FollowPK followPK) {
        this.followPK = followPK;
    }

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FollowPK implements Serializable {

        private String targetEmail;
        private String followerEmail;

        public FollowPK(String targetEmail, String followerEmail) {
            this.targetEmail = targetEmail;
            this.followerEmail = followerEmail;
        }
    }
}