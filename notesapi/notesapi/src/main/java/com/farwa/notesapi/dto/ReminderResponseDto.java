package com.farwa.notesapi.dto;

import com.farwa.notesapi.model.RepeatType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ReminderResponseDto {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private RepeatType repeatType;
    private Long noteId;
}