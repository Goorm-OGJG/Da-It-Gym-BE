package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.comment.routine.exception.NotFoundRoutine;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineListResponseDto getRoutines(Pageable pageable) {

        Slice<Routine> routines = routineRepository.findAll(pageable)
                .orElseThrow(NotFoundRoutine::new);

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
}
