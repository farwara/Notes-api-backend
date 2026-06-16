package com.farwa.notesapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    private Long userId;

    private Long categoryId;

    private List<Long> tagIds;
}