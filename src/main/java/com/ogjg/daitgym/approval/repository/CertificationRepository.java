package com.ogjg.daitgym.approval.repository;

import com.ogjg.daitgym.domain.Certification;
import com.ogjg.daitgym.domain.CertificationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUserEmail(String email);
}
