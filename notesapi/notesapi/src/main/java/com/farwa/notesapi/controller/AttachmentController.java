package com.farwa.notesapi.controller;

import com.farwa.notesapi.dto.AttachmentResponseDto;
import com.farwa.notesapi.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/{noteId}/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public AttachmentResponseDto uploadAttachment(
            @PathVariable Long noteId,
            @RequestParam("file") MultipartFile file
    ) {
        return attachmentService.uploadAttachment(noteId, file);
    }

    @GetMapping("/{noteId}/download")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long noteId) {
        Resource resource = attachmentService.downloadAttachment(noteId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    @GetMapping("/{noteId}")
    public AttachmentResponseDto getAttachmentInfo(@PathVariable Long noteId) {
        return attachmentService.getAttachmentInfo(noteId);
    }
    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttachment(@PathVariable Long noteId) {
        attachmentService.deleteAttachment(noteId);
    }
}
