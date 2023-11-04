package com.ogjg.daitgym.follow.repository;

import com.ogjg.daitgym.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK>, FollowRepositoryCustom {

    int countByFollowPKTargetEmail(String targetEmail);

    int countByFollowPKFollowerEmail(String followerEmail);
}
