package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.NoteRequestDto;
import com.farwa.notesapi.dto.NoteResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.*;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import com.farwa.notesapi.repository.NoteRepository;
import com.farwa.notesapi.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public NoteResponseDto createNote(NoteRequestDto requestDto) {
        AppUser user = appUserRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = null;
        if (requestDto.getCategoryId() != null) {
            category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        List<Tag> tags = List.of();
        if (requestDto.getTagIds() != null && !requestDto.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(requestDto.getTagIds());
        }

        Note note = Note.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .category(category)
                .tags(tags)
                .status(NoteStatus.ACTIVE)
                .build();

        Note savedNote = noteRepository.save(note);

        return mapToResponseDto(savedNote);
    }

    public List<NoteResponseDto> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public NoteResponseDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        return mapToResponseDto(note);
    }

    public List<NoteResponseDto> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public NoteResponseDto updateNote(Long id, NoteRequestDto requestDto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        note.setTitle(requestDto.getTitle());
        note.setContent(requestDto.getContent());

        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            note.setCategory(category);
        }

        if (requestDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(requestDto.getTagIds());
            note.setTags(tags);
        }

        Note updatedNote = noteRepository.save(note);

        return mapToResponseDto(updatedNote);
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found");
        }

        noteRepository.deleteById(id);
    }

    private NoteResponseDto mapToResponseDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .status(note.getStatus())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .userId(note.getUser().getId())
                .categoryName(note.getCategory() != null ? note.getCategory().getName() : null)
                .tags(note.getTags().stream().map(Tag::getName).toList())
                .build();
    }
}