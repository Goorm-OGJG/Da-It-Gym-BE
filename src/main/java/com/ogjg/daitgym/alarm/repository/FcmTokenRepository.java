package com.ogjg.daitgym.alarm.repository;

import com.ogjg.daitgym.domain.FcmToken;
import com.ogjg.daitgym.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    boolean existsByUserAndToken(User user, String token);

    Optional<FcmToken> findByUserAndToken(User user, String token);
}
