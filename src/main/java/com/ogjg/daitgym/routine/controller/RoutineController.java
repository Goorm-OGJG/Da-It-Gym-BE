package com.ogjg.daitgym.routine.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ApiResponse<RoutineListResponseDto> getRoutines(
            Pageable pageable,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        RoutineListResponseDto routines = routineService.getRoutines(pageable, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routines);
    }

    @GetMapping("/{userEmail}")
    public ApiResponse<RoutineListResponseDto> getUserRoutines(
            @PathVariable("userEmail") String userEmail,
            Pageable pageable) {
        RoutineListResponseDto userRoutines = routineService.getUserRoutines(userEmail, pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, userRoutines);
    }

    @GetMapping("/following")
    public ApiResponse<RoutineListResponseDto> getFollowerRoutines(
            Pageable pageable,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        RoutineListResponseDto routinesOfFollowing = routineService.getFollowerRoutines(pageable, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routinesOfFollowing);
    }

    @GetMapping("/{routineId}/details")
    public ApiResponse<RoutineDetailsResponseDto> getRoutineDetails(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        RoutineDetailsResponseDto routineDetails = routineService.getRoutineDetails(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routineDetails);
    }
}
