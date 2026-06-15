package com.farwa.notesapi.repository;

import com.farwa.notesapi.model.Note;
import com.farwa.notesapi.model.NoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
    List<Note> findByUserIdAndStatus(Long userId, NoteStatus status);
    List<Note> findByTitleContainingIgnoreCase(String title);
}