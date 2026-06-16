package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.ReminderRequestDto;
import com.farwa.notesapi.dto.ReminderResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.Note;
import com.farwa.notesapi.model.Reminder;
import com.farwa.notesapi.model.RepeatType;
import com.farwa.notesapi.repository.NoteRepository;
import com.farwa.notesapi.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final NoteRepository noteRepository;

    public ReminderResponseDto createReminder(ReminderRequestDto requestDto) {
        Note note = noteRepository.findById(requestDto.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        Reminder reminder = Reminder.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .repeatType(requestDto.getRepeatType() != null ? requestDto.getRepeatType() : RepeatType.NONE)
                .note(note)
                .build();

        return mapToResponseDto(reminderRepository.save(reminder));
    }

    public List<ReminderResponseDto> getRemindersByNoteId(Long noteId) {
        return reminderRepository.findByNoteId(noteId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ReminderResponseDto updateReminder(Long id, ReminderRequestDto requestDto) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));

        reminder.setDate(requestDto.getDate());
        reminder.setTime(requestDto.getTime());
        reminder.setRepeatType(requestDto.getRepeatType() != null ? requestDto.getRepeatType() : RepeatType.NONE);

        return mapToResponseDto(reminderRepository.save(reminder));
    }

    public void deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reminder not found");
        }

        reminderRepository.deleteById(id);
    }

    private ReminderResponseDto mapToResponseDto(Reminder reminder) {
        return ReminderResponseDto.builder()
                .id(reminder.getId())
                .date(reminder.getDate())
                .time(reminder.getTime())
                .repeatType(reminder.getRepeatType())
                .noteId(reminder.getNote().getId())
                .build();
    }
}