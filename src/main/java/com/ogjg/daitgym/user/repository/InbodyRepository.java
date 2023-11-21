package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.Inbody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InbodyRepository extends JpaRepository<Inbody, Long> {
    List<Inbody> findByUserEmail(String email);
    Optional<Inbody> findFirstByUserEmailOrderByCreatedAtDesc(String email);
}
