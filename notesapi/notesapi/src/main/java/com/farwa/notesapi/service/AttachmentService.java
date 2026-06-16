package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.AttachmentResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.Attachment;
import com.farwa.notesapi.model.Note;
import com.farwa.notesapi.repository.AttachmentRepository;
import com.farwa.notesapi.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final NoteRepository noteRepository;

    private final Path uploadPath = Paths.get("src/main/resources/uploads");

    public AttachmentResponseDto uploadAttachment(Long noteId, MultipartFile file) {
        try {
            Note note = noteRepository.findById(noteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

            Files.createDirectories(uploadPath);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            Attachment attachment = Attachment.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .note(note)
                    .build();

            return mapToResponseDto(attachmentRepository.save(attachment));

        } catch (Exception e) {
            throw new RuntimeException("Could not upload file: " + e.getMessage());
        }
    }

    public Resource downloadAttachment(Long noteId) {
        try {
            Attachment attachment = attachmentRepository.findByNoteId(noteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFoundException("File not found");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not download file");
        }
    }

    public AttachmentResponseDto getAttachmentInfo(Long noteId) {
        Attachment attachment = attachmentRepository.findByNoteId(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        return mapToResponseDto(attachment);
    }

    private AttachmentResponseDto mapToResponseDto(Attachment attachment) {
        return AttachmentResponseDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .uploadDate(attachment.getUploadDate())
                .noteId(attachment.getNote().getId())
                .build();
    }
}