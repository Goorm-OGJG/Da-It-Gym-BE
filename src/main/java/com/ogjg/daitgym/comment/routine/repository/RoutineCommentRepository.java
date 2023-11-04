package com.ogjg.daitgym.comment.routine.repository;

import com.ogjg.daitgym.domain.RoutineComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineCommentRepository extends JpaRepository<RoutineComment, Long> {

    Page<RoutineComment> findByRoutineIdAndParentIdIsNull(Long routineId, Pageable pageable);

    List<RoutineComment> findByRoutineIdAndParentId(Long routineId, Long parentId);

    Optional<RoutineComment> findByRoutineIdAndId(Long routineId, Long commentId);

    int countByRoutineIdAndParentIdIsNotNull(Long routineId);

    int countByRoutineIdAndParentIdIsNull(Long routineId);
}