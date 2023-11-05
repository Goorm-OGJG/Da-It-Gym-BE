package com.ogjg.daitgym.like.routine.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundUser;
import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.Routine;
import com.ogjg.daitgym.domain.RoutineLike;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.like.routine.dto.RoutineLikeResponse;
import com.ogjg.daitgym.like.routine.repository.RoutineLikeRepository;
import com.ogjg.daitgym.like.routine.repository.RoutineRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineLikeService {
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final RoutineLikeRepository routineLikeRepository;

    @Transactional
    public RoutineLikeResponse routineLike(Long routineId) {
        User user = getUserByEmail("yaejingo@gmail.com");
        Routine routine = routineRepository.findById(routineId).orElseThrow(NotFoundRoutine::new);

        if (!routineLikeRepository.existsByUserEmailAndRoutineId(user.getEmail(),routineId)) {
            routineLikeRepository.save(new RoutineLike(user, routine));
        }
        int likeCount = routineLikeRepository.countByRoutineLikePkRoutineId(routineId);

        return new RoutineLikeResponse(likeCount);
    }

    @Transactional
    public RoutineLikeResponse routineUnLike(Long routineId) {
        User user = getUserByEmail("yaejingo@gmail.com");
        Routine routine = routineRepository.findById(routineId).orElseThrow(NotFoundRoutine::new);

        if (routineLikeRepository.existsByUserEmailAndRoutineId(user.getEmail(),routineId)) {
            routineLikeRepository.delete(new RoutineLike(user, routine));
        }
        int likeCount = routineLikeRepository.countByRoutineLikePkRoutineId(routineId);
        return new RoutineLikeResponse(likeCount);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }
}
