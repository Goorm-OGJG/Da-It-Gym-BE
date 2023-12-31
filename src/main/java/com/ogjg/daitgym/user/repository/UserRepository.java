package com.ogjg.daitgym.user.repository;

import com.ogjg.daitgym.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByNickname(String nickName);

    Optional<User> findByEmail(String email);

    @Query("""
    select u from User u where u.email = :email
""")
    Optional<User> findByEmailIncludingDeleted(@Param("email") String email);

    Page<User> findByNicknameStartingWith(String nickname, Pageable pageable);

    List<User> findByNicknameStartingWith(String nickname);
}
