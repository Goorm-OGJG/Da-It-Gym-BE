package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.routine.Day;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.like.routine.repository.RoutineLikeRepository;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.exception.NoExerciseInRoutine;
import com.ogjg.daitgym.routine.repository.DayRepository;
import com.ogjg.daitgym.routine.repository.ExerciseDetailRepository;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final FollowRepository followRepository;
    private final DayRepository dayRepository;
    private final ExerciseDetailRepository exerciseDetailRepository;
    private final RoutineLikeRepository routineLikeRepository;

    @Transactional(readOnly = true)
    public RoutineListResponseDto getRoutines(Pageable pageable, Integer division, String email) {

        Slice<Routine> routines = routineRepository.findAllByDivision(division, pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines, email);
    }

    private RoutineListResponseDto getRoutineListResponseDto(Slice<Routine> routines, String email) {
        if (!routines.hasNext()) {
            throw new NotFoundRoutine("더 이상 루틴이 존재하지 않습니다.");
        }

        Set<Long> likedRoutineIdByUserEmail = routineLikeRepository.findLikedRoutineIdByUserEmail(email);

        List<RoutineDto> routineDtos = routines.stream()
                .map(routine -> RoutineDto.builder()
                        .id(routine.getId())
                        .title(routine.getTitle())
                        .author(routine.getUser().getNickname())
                        .description(routine.getContent())
                        .liked(likedRoutineIdByUserEmail.contains(routine.getId()))
                        .likeCounts(routine.getLikesCount())
//                            .scrapCounts()
                        .createdAt(routine.getCreatedAt())
                        .build())
                .toList();

        return RoutineListResponseDto.builder()
                .routines(routineDtos)
                .currentPage(routines.getNumber())
                .hasNext(routines.hasNext())
                .build();
    }

    @Transactional(readOnly = true)
    public RoutineListResponseDto getUserRoutines(String userEmail, Integer division, Pageable pageable) {
        Slice<Routine> routines = routineRepository.findByDivisionAndUserEmail(division, userEmail, pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines, userEmail);
    }

    @Transactional(readOnly = true)
    public RoutineListResponseDto getFollowerRoutines(Pageable pageable, Integer division, String myEmail) {

        List<String> followingEmails = followRepository.findAllByTargetEmail(myEmail)
                .orElse(Collections.emptyList())
                .stream()
                .map(follow -> follow.getTarget().getEmail())
                .toList();

        Slice<Routine> routines = routineRepository.findByDivisionAndUserEmailIn(division, followingEmails, pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines, myEmail);
    }

    @Transactional(readOnly = true)
    public RoutineDetailsResponseDto getRoutineDetails(Long routineId, String userEmail) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(NotFoundRoutine::new);

        List<Day> days = dayRepository.findAllWithExerciseDetailsByRoutineId(routine.getId())
                .orElseThrow(NoExerciseInRoutine::new);

        List<DayDto> dayDtos = getDayDtos(days);

        return RoutineDetailsResponseDto.builder()
                .writer(routine.getUser().getNickname())
                .writerImg(routine.getUser().getImageUrl())
                .title(routine.getTitle())
                .description(routine.getContent())
                .liked(routineLikeRepository
                        .existsByUserEmailAndRoutineId(userEmail, routine.getId()))
                .likeCounts(routine.getLikesCount())
                .scrapCounts(0)
                .routine(RoutineDetailsResponseDto.RoutineDto.builder()
                        .id(routine.getId())
                        .days(dayDtos)
                        .build())
                .createdAt(routine.getCreatedAt())
                .build();
    }

    private static List<DayDto> getDayDtos(List<Day> days) {
        List<DayDto> dayDtos = days.stream()
                .map(day -> {
                    List<ExerciseDto> exerciseDtos = day.getExerciseDetails().stream()
                            .map(exerciseDetail -> {
                                ExerciseSets exerciseSet = ExerciseSets.builder()
                                        .id(exerciseDetail.getId())
                                        .order(exerciseDetail.getSetCount())
                                        .weights(exerciseDetail.getWeight())
                                        .counts(exerciseDetail.getRepetitionCount())
                                        .completed(false)
                                        .build();

                                return ExerciseDto.builder()
                                        .id(exerciseDetail.getExercise().getId())
                                        .order(exerciseDetail.getExerciseOrder())
                                        .name(exerciseDetail.getExercise().getName())
                                        .part(exerciseDetail.getExercise().getExercisePart().getPart())
                                        .restTime(new RestTimeDto(
                                                exerciseDetail.getRestTime().getHour(),
                                                exerciseDetail.getRestTime().getMinute(),
                                                exerciseDetail.getRestTime().getSecond()))
                                        .exerciseSets(Collections.singletonList(exerciseSet))
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return DayDto.builder()
                            .id(day.getId())
                            .order(day.getDayNumber())
                            .isSpread(false)
                            .exercises(exerciseDtos)
                            .build();
                })
                .toList();
        return dayDtos;
    }
}