package com.ogjg.daitgym.journal.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class ReplicationRoutineRequest {

    List<ReplicationRoutineRequestDto> routines = new ArrayList<>();
}
