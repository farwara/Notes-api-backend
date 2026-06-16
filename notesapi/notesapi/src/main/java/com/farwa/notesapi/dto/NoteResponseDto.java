package com.farwa.notesapi.dto;

import com.farwa.notesapi.model.NoteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class NoteResponseDto {

    private Long id;
    private String title;
    private String content;
    private NoteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String categoryName;
    private List<String> tags;
}