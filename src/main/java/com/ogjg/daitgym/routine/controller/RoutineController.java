package com.ogjg.daitgym.routine.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.routine.dto.RoutineDetailsResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.dto.RoutineRequestDto;
import com.ogjg.daitgym.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    @GetMapping
    public ApiResponse<RoutineListResponseDto> getRoutines(
            Pageable pageable,
            @RequestParam(value = "division", required = false) Integer division,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        RoutineListResponseDto routines = routineService.getRoutines(pageable, division, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routines);
    }

    @GetMapping("/{userEmail}")
    public ApiResponse<RoutineListResponseDto> getUserRoutines(
            @PathVariable("userEmail") String userEmail,
            Pageable pageable,
            @RequestParam(value = "division", required = false) Integer division) {
        RoutineListResponseDto userRoutines = routineService.getUserRoutines(userEmail, division, pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, userRoutines);
    }

    @GetMapping("/following")
    public ApiResponse<RoutineListResponseDto> getFollowerRoutines(
            Pageable pageable,
            @RequestParam(value = "division", required = false) Integer division,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        RoutineListResponseDto routinesOfFollowing = routineService.getFollowerRoutines(pageable, division, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routinesOfFollowing);
    }

    @GetMapping("/{routineId}/details")
    public ApiResponse<RoutineDetailsResponseDto> getRoutineDetails(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        RoutineDetailsResponseDto routineDetails = routineService.getRoutineDetails(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routineDetails);
    }

    @PostMapping
    public ApiResponse<Void> createRoutine(
            @RequestBody RoutineRequestDto routineRequestDto,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        routineService.createRoutine(routineRequestDto, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    @DeleteMapping("/{routineId}")
    public ApiResponse<Void> deleteRoutine(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        routineService.deleteRoutine(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    @GetMapping("/scrap/{routineId}")
    public ApiResponse<Void> scrapRoutine(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        routineService.scrapRoutine(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    @DeleteMapping("/scrap/{routineId}")
    public ApiResponse<Void> unscrapRoutine(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        routineService.unscrapRoutine(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
