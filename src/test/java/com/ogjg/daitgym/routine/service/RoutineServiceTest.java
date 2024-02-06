package com.ogjg.daitgym.routine.service;

import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.routine.Routine;
import com.ogjg.daitgym.like.routine.repository.RoutineLikeRepository;
import com.ogjg.daitgym.routine.dto.RoutineListResponseDto;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import com.ogjg.daitgym.routine.repository.UserRoutineCollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private RoutineLikeRepository routineLikeRepository;

    @Mock
    private UserRoutineCollectionRepository userRoutineCollectionRepository;

    @InjectMocks
    private RoutineService routineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoutines() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Integer division = 1;
        String email = "user@example.com";

        // User 모의 객체 생성
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getNickname()).thenReturn("exampleNickname");

        // Routine 모의 객체 생성 및 설정
        Routine mockRoutine1 = Mockito.mock(Routine.class);
        when(mockRoutine1.getUser()).thenReturn(mockUser);
        Routine mockRoutine2 = Mockito.mock(Routine.class);
        when(mockRoutine2.getUser()).thenReturn(mockUser);
        List<Routine> mockRoutines = Arrays.asList(mockRoutine1, mockRoutine2);
        Slice<Routine> routineSlice = new SliceImpl<>(mockRoutines, pageable, false);

        // routineRepository.findAllByDivision에 대한 모의 동작 정의
        when(routineRepository.findAllByDivision(division, pageable)).thenReturn(Optional.of(routineSlice));

        // routineLikeRepository에 대한 모의 동작 정의
        Set<Long> likedRoutineIds = new HashSet<>(Arrays.asList(1L, 2L));
        when(routineLikeRepository.findLikedRoutineIdByUserEmail(email)).thenReturn(likedRoutineIds);

        // When
        RoutineListResponseDto result = routineService.getRoutines(pageable, division, email);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getRoutines().size());
    }
}
