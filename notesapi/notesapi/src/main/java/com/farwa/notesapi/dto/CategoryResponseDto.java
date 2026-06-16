package com.farwa.notesapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String color;
    private Long createdByAdminId;
}
