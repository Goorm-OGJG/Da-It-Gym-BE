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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    @GetMapping
    public ApiResponse<RoutineListResponseDto> getRoutines(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "division", required = false) Integer division,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        RoutineListResponseDto routines = routineService.getRoutines(pageable, division, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, routines);
    }

    @GetMapping("/{nickname}")
    public ApiResponse<RoutineListResponseDto> getUserRoutines(
            @PathVariable("nickname") String nickname,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "division", required = false) Integer division) {
        RoutineListResponseDto userRoutines = routineService.getUserRoutines(nickname, division, pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, userRoutines);
    }

    @GetMapping("/following")
    public ApiResponse<RoutineListResponseDto> getFollowerRoutines(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
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

    @PostMapping("/{routineId}/scrap")
    public ApiResponse<Map<String, Long>> scrapRoutine(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        Long scrappedCounts = routineService.scrapRoutine(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, Map.of("scrapCnt", scrappedCounts));
    }

    @DeleteMapping("/{routineId}/scrap")
    public ApiResponse<Map<String, Long>> unscrapRoutine(
            @PathVariable("routineId") Long routineId,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        Long scrappedCounts = routineService.unscrapRoutine(routineId, oAuth2JwtUserDetails.getEmail());

        return new ApiResponse<>(ErrorCode.SUCCESS, Map.of("scrapCnt", scrappedCounts));
    }

    @GetMapping("/scraps")
    public ApiResponse<RoutineListResponseDto> getScrappedRoutines(
            Pageable pageable,
            @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails
    ) {
        RoutineListResponseDto scrappedRoutines = routineService.getScrappedRoutines(oAuth2JwtUserDetails.getEmail(), pageable);

        return new ApiResponse<>(ErrorCode.SUCCESS, scrappedRoutines);
    }
}
