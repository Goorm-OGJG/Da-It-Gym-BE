package com.ogjg.daitgym.comment.routine.repository;

import com.ogjg.daitgym.domain.routine.RoutineComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineCommentRepository extends JpaRepository<RoutineComment, Long> {


    Optional<RoutineComment> findByRoutineIdAndId(Long routineId, Long commentId);

    int countByRoutineIdAndParentIdIsNotNull(Long routineId);

    int countByRoutineIdAndParentIdIsNull(Long routineId);

    Page<RoutineComment> findByRoutineIdAndParentIdIsNullOrderByCreatedAtDesc(Long routineId, Pageable pageable);

    List<RoutineComment> findByRoutineIdAndParentIdOrderByCreatedAtDesc(Long routineId, Long commentId);
}