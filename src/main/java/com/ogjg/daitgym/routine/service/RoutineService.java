package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.routine.Day;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.exception.NoExerciseInRoutine;
import com.ogjg.daitgym.routine.repository.DayRepository;
import com.ogjg.daitgym.routine.repository.ExerciseDetailRepository;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;
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

    @Transactional(readOnly = true)
    public RoutineListResponseDto getRoutines(Pageable pageable) {

        Slice<Routine> routines = routineRepository.findAll(pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines);
    }

    private RoutineListResponseDto getRoutineListResponseDto(Slice<Routine> routines) {
        if (!routines.hasNext()) {
            throw new NotFoundRoutine("더 이상 루틴이 존재하지 않습니다.");
        }

        List<RoutineDto> routineDtos = routines.stream()
                .map(routine -> RoutineDto.builder()
                        .id(routine.getId())
                        .title(routine.getTitle())
                        .author(routine.getUser().getNickname())
                        .description(routine.getContent())
//                            .likeCounts()
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
    public RoutineListResponseDto getUserRoutines(String userEmail, Pageable pageable) {
        Slice<Routine> routines = routineRepository.findAllByUserEmail(userEmail, pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines);
    }

    @Transactional(readOnly = true)
    public RoutineListResponseDto getFollowerRoutines(String myEmail, Pageable pageable) {

        List<String> followingEmails = followRepository.findAllByTargetEmail(myEmail)
                .orElse(Collections.emptyList())
                .stream()
                .map(follow -> follow.getTarget().getEmail())
                .toList();

        Slice<Routine> routines = routineRepository.findByUserEmailIn(followingEmails, pageable)
                .orElseThrow(NotFoundRoutine::new);

        return getRoutineListResponseDto(routines);
    }

    @Transactional(readOnly = true)
    public RoutineDetailsResponseDto getRoutineDetails(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(NotFoundRoutine::new);

        List<Day> days = dayRepository.findAllWithExerciseDetailsByRoutineId(routine.getId())
                .orElseThrow(NoExerciseInRoutine::new);

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
                                        .order(exerciseDetail.getOrder())
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

        return RoutineDetailsResponseDto.builder()
                .writer(routine.getUser().getNickname())
                .writerImg(routine.getUser().getImageUrl())
                .title(routine.getTitle())
                .description(routine.getContent())
                .liked(false)
                .likeCounts(0)
                .scrapCounts(0)
                .routine(RoutineDetailsResponseDto.RoutineDto.builder()
                        .id(routine.getId())
                        .days(dayDtos)
                        .build())
                .createdAt(routine.getCreatedAt())
                .build();
    }
}
