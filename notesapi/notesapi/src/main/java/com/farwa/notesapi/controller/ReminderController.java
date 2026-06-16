package com.farwa.notesapi.controller;

import com.farwa.notesapi.dto.ReminderRequestDto;
import com.farwa.notesapi.dto.ReminderResponseDto;
import com.farwa.notesapi.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponseDto createReminder(@Valid @RequestBody ReminderRequestDto requestDto) {
        return reminderService.createReminder(requestDto);
    }

    @GetMapping("/note/{noteId}")
    public List<ReminderResponseDto> getRemindersByNoteId(@PathVariable Long noteId) {
        return reminderService.getRemindersByNoteId(noteId);
    }

    @PutMapping("/{id}")
    public ReminderResponseDto updateReminder(
            @PathVariable Long id,
            @Valid @RequestBody ReminderRequestDto requestDto
    ) {
        return reminderService.updateReminder(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
    }
}