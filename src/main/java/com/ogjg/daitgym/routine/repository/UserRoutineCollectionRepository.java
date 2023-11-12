package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.domain.routine.UserRoutineCollection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static com.ogjg.daitgym.domain.routine.UserRoutineCollection.*;

public interface UserRoutineCollectionRepository extends JpaRepository<UserRoutineCollection, PK> {

    @Query("SELECT urc.routine FROM UserRoutineCollection urc WHERE urc.pk.email = :email")
    Slice<Routine> findRoutinesByUserEmail(@Param("email") String email, Pageable pageable);

}
