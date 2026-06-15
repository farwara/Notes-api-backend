package com.farwa.notesapi.repository;

import com.farwa.notesapi.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByNoteId(Long noteId);
}