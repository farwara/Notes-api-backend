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
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final NoteRepository noteRepository;

    private final Path uploadPath =
            Paths.get("src/main/resources/uploads");

    public AttachmentResponseDto uploadAttachment(
            Long noteId,
            MultipartFile file
    ) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Note not found"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a file to upload"
            );
        }

        try {
            Files.createDirectories(uploadPath);

            String originalFileName = file.getOriginalFilename();

            String safeFileName;

            if (originalFileName == null ||
                    originalFileName.isBlank()) {

                safeFileName = "attachment";

            } else {
                safeFileName = Paths.get(originalFileName)
                        .getFileName()
                        .toString();
            }

            String storedFileName =
                    System.currentTimeMillis()
                            + "_"
                            + safeFileName;

            Path filePath = uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Attachment attachment = Attachment.builder()
                    .fileName(storedFileName)
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .note(note)
                    .build();

            Attachment savedAttachment =
                    attachmentRepository.save(attachment);

            return mapToResponseDto(savedAttachment);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not upload file: " + e.getMessage(),
                    e
            );
        }
    }

    public Resource downloadAttachment(Long noteId) {
        Attachment attachment =
                attachmentRepository.findByNoteId(noteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found"
                                ));

        try {
            Path filePath =
                    Paths.get(attachment.getFilePath());

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists() ||
                    !resource.isReadable()) {

                throw new ResourceNotFoundException(
                        "File not found"
                );
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Could not download file",
                    e
            );
        }
    }

    public AttachmentResponseDto getAttachmentInfo(
            Long noteId
    ) {
        Attachment attachment =
                attachmentRepository.findByNoteId(noteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found"
                                ));

        return mapToResponseDto(attachment);
    }

    public void deleteAttachment(Long noteId) {
        Attachment attachment =
                attachmentRepository.findByNoteId(noteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment not found"
                                ));

        try {
            Path filePath =
                    Paths.get(attachment.getFilePath());

            Files.deleteIfExists(filePath);

            attachmentRepository.delete(attachment);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not delete attachment: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private AttachmentResponseDto mapToResponseDto(
            Attachment attachment
    ) {
        return AttachmentResponseDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .uploadDate(attachment.getUploadDate())
                .noteId(attachment.getNote().getId())
                .build();
    }
}