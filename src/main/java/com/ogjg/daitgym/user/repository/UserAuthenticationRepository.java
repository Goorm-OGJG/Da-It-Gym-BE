package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByUserEmail(String email);
    Optional<UserAuthentication> findByProviderId(Long Id);
}
