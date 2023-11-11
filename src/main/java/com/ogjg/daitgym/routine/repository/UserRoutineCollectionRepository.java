package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.domain.routine.UserRoutineCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.ogjg.daitgym.domain.routine.UserRoutineCollection.*;

public interface UserRoutineCollectionRepository extends JpaRepository<UserRoutineCollection, PK> {
}
