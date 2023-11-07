package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.Inbody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InbodyRepository extends JpaRepository<Inbody, Long> {
    List<Inbody> findByUserEmail(String email);
}
