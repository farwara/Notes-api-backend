package com.farwa.notesapi.dto;

import com.farwa.notesapi.model.RepeatType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReminderRequestDto {

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Reminder date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    private RepeatType repeatType;

    @NotNull(message = "Note id is required")
    private Long noteId;
}