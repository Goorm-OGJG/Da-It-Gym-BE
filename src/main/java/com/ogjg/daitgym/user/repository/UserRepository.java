package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByNickname(String nickName);

    Optional<User> findByEmail(String email);
}
