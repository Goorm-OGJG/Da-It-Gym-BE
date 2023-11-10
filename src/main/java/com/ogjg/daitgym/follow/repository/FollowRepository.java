package com.ogjg.daitgym.follow.repository;

import com.ogjg.daitgym.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK>, FollowRepositoryCustom {

    int countByFollowPKTargetEmail(String targetEmail);

    int countByFollowPKFollowerEmail(String followerEmail);

    Optional<List<Follow>> findAllByTargetEmail(String followingEmail);

}

