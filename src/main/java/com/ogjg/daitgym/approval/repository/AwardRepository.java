package com.ogjg.daitgym.approval.repository;

import com.ogjg.daitgym.domain.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long> {
    List<Award> findByUserEmail(String email);
}
