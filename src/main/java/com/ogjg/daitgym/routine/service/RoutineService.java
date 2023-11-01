package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final FollowRepository followRepository;

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
                .map(routine -> {
                    return RoutineDto.builder()
                            .id(routine.getId())
                            .title(routine.getTitle())
                            .author(routine.getUser().getNickname())
                            .description(routine.getContent())
//                            .likeCounts()
//                            .scrapCounts()
                            .createdAt(routine.getCreatedAt())
                            .build();
                })
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

}
