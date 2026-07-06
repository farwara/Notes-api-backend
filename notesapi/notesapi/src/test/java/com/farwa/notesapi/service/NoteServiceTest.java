package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.NoteRequestDto;
import com.farwa.notesapi.dto.NoteResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Note;
import com.farwa.notesapi.model.NoteStatus;
import com.farwa.notesapi.model.Role;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import com.farwa.notesapi.repository.NoteRepository;
import com.farwa.notesapi.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void createNote_shouldReturnCreatedNote() {
        AppUser user = AppUser.builder()
                .id(1L)
                .name("Test User")
                .email("user@test.com")
                .role(Role.USER)
                .build();

        NoteRequestDto request = new NoteRequestDto();
        request.setTitle("Test note");
        request.setContent("Test content");
        request.setUserId(1L);

        Note savedNote = Note.builder()
                .id(1L)
                .title("Test note")
                .content("Test content")
                .status(NoteStatus.ACTIVE)
                .user(user)
                .tags(List.of())
                .build();

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        NoteResponseDto response = noteService.createNote(request);

        assertEquals("Test note", response.getTitle());
        assertEquals("Test content", response.getContent());
        assertEquals(1L, response.getUserId());

        verify(appUserRepository).findById(1L);
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void getNoteById_shouldReturnNote_whenNoteExists() {
        AppUser user = AppUser.builder()
                .id(1L)
                .name("Test User")
                .email("user@test.com")
                .role(Role.USER)
                .build();

        Note note = Note.builder()
                .id(1L)
                .title("Existing note")
                .content("Content")
                .status(NoteStatus.ACTIVE)
                .user(user)
                .tags(List.of())
                .build();

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        NoteResponseDto response = noteService.getNoteById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Existing note", response.getTitle());

        verify(noteRepository).findById(1L);
    }

    @Test
    void getNoteById_shouldThrowException_whenNoteDoesNotExist() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteById(99L));

        verify(noteRepository).findById(99L);
    }

    @Test
    void deleteNote_shouldDeleteNote_whenNoteExists() {
        when(noteRepository.existsById(1L)).thenReturn(true);

        noteService.deleteNote(1L);

        verify(noteRepository).existsById(1L);
        verify(noteRepository).deleteById(1L);
    }

    @Test
    void deleteNote_shouldThrowException_whenNoteDoesNotExist() {
        when(noteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> noteService.deleteNote(99L));

        verify(noteRepository).existsById(99L);
        verify(noteRepository, never()).deleteById(99L);
    }
}