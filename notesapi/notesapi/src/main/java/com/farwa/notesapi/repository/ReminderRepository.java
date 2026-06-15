package com.farwa.notesapi.repository;

import com.farwa.notesapi.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByNoteId(Long noteId);
}