package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.domain.routine.Routine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("SELECT r FROM Routine r WHERE (:division IS NULL OR r.division = :division)")
    Optional<Slice<Routine>> findAllByDivision(@Param("division") Integer division, Pageable pageable);

    @Query("SELECT r FROM Routine r WHERE (:division IS NULL OR r.division = :division) AND r.user.email = :email")
    Optional<Slice<Routine>> findByDivisionAndUserEmail(@Param("division") Integer division, @Param("email") String email, Pageable pageable);

    @Query("SELECT r FROM Routine r WHERE (:division IS NULL OR r.division = :division) AND r.user.email IN :followerEmails")
    Optional<Slice<Routine>> findByDivisionAndUserEmailIn(@Param("division") Integer division, @Param("followerEmails") List<String> followerEmails, Pageable pageable);

//    Optional<Routine> findById(Long routineId);

}
