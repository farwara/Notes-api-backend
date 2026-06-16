package com.farwa.notesapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequestDto {

    @NotBlank(message = "Tag name is required")
    private String name;

    private String color;

    private Long userId;
}