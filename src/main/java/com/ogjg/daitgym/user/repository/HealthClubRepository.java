package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.HealthClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthClubRepository extends JpaRepository<HealthClub, Long> {
    List<HealthClub> findByName(String gymName);
}
