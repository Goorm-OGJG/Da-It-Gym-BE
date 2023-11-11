package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.TimeTemplate;
import com.ogjg.daitgym.domain.routine.Day;
import com.ogjg.daitgym.domain.routine.ExerciseDetail;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.exercise.exception.NotFoundExercise;
import com.ogjg.daitgym.exercise.repository.ExerciseRepository;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.like.routine.repository.RoutineLikeRepository;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineRequestDto;
import com.ogjg.daitgym.routine.exception.NoExerciseInRoutine;
import com.ogjg.daitgym.routine.repository.DayRepository;
import com.ogjg.daitgym.routine.repository.ExerciseDetailRepository;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.exception.UnauthorizedUser;
import com.ogjg.daitgym.user.repository.UserRepository;
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
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

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
                                                exerciseDetail.getRestTime().getHours(),
                                                exerciseDetail.getRestTime().getMinutes(),
                                                exerciseDetail.getRestTime().getSeconds()))
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

    @Transactional
    public void createRoutine(RoutineRequestDto routineRequestDto, String email) {

        Routine routine = Routine.builder()
                .user(userRepository.findByEmail(email)
                        .orElseThrow(NotFoundUser::new))
                .title(routineRequestDto.getTitle())
                .content(routineRequestDto.getDescription())
                .duration(routineRequestDto.getRoutine().getDays().size())
                .build();

        routineRepository.save(routine);

        routineRequestDto.getRoutine().getDays().forEach(dayDto -> {
            Day day = Day.builder()
                    .routine(routine)
                    .dayNumber(dayDto.getOrder())
                    .build();

            dayRepository.save(day);

            dayDto.getExercises().forEach(exerciseDto -> {
                exerciseDto.getExerciseSets().forEach(exerciseSetDto -> {
                    RoutineRequestDto.RestTimeDto restTime = exerciseDto.getRestTime();
                    TimeTemplate timeTemplate = new TimeTemplate(restTime.getHours(), restTime.getMinutes(), restTime.getSeconds());
                    ExerciseDetail exerciseDetail = ExerciseDetail.builder()
                            .day(day)
                            .exercise(exerciseRepository.findByName(exerciseDto.getName())
                                    .orElseThrow(NotFoundExercise::new))
                            .exerciseOrder(exerciseDto.getOrder())
                            .setCount(exerciseSetDto.getOrder())
                            .repetitionCount(exerciseSetDto.getCounts())
                            .weight(exerciseSetDto.getWeights())
                            .restTime(timeTemplate)
                            .build();

                    day.addExerciseDetail(exerciseDetail);
                });
            });
        });
    }

    @Transactional
    public void deleteRoutine(Long routineId, String email) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(NotFoundRoutine::new);

        if (!routine.getUser().getEmail().equals(email)) {
            throw new UnauthorizedUser("사용자에게 루틴을 삭제할 권한이 없습니다.");
        }

        routineRepository.deleteById(routineId);
    }
}
