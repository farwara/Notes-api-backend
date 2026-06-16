package com.farwa.notesapi.repository;

import com.farwa.notesapi.model.Note;
import com.farwa.notesapi.model.NoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
    List<Note> findByUserIdAndStatus(Long userId, NoteStatus status);
    List<Note> findByTitleContainingIgnoreCase(String title);

    @Query("""
            SELECT DISTINCT n FROM Note n
            LEFT JOIN n.category c
            LEFT JOIN n.tags t
            WHERE (:title IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:category IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%')))
            AND (:tag IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tag, '%')))
            """)
    List<Note> searchNotes(
            @Param("title") String title,
            @Param("category") String category,
            @Param("tag") String tag
    );
}