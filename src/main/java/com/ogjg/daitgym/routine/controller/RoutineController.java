package com.ogjg.daitgym.routine.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import com.ogjg.daitgym.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    @GetMapping
    public ApiResponse<RoutineListResponseDto> getRoutines(Pageable pageable) {
        RoutineListResponseDto routines = routineService.getRoutines(pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, routines);
    }

    @GetMapping("/{userEmail}")
    public ApiResponse<RoutineListResponseDto> getUserRoutines(@PathVariable("userEmail") String userEmail,
                                          Pageable pageable) {
        RoutineListResponseDto userRoutines = routineService.getUserRoutines(userEmail, pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, userRoutines);
    }

    @GetMapping("/following")
    public ApiResponse<RoutineListResponseDto> getFollowerRoutines(String myEmail, Pageable pageable) {

        RoutineListResponseDto routinesOfFollowing = routineService.getFollowerRoutines(myEmail, pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, routinesOfFollowing);
    }

    @GetMapping("/{routineId}/details")
    public ApiResponse<RoutineDetailsResponseDto> getRoutineDetails(@PathVariable("routineId") Long routineId) {

        RoutineDetailsResponseDto routineDetails = routineService.getRoutineDetails(routineId);

        return new ApiResponse<>(ErrorCode.SUCCESS, routineDetails);
    }
}
