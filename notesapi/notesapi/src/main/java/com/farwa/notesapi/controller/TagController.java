package com.farwa.notesapi.controller;

import com.farwa.notesapi.dto.TagRequestDto;
import com.farwa.notesapi.dto.TagResponseDto;
import com.farwa.notesapi.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseDto createTag(@Valid @RequestBody TagRequestDto requestDto) {
        return tagService.createTag(requestDto);
    }

    @GetMapping("/user/{userId}")
    public List<TagResponseDto> getTagsByUserId(@PathVariable Long userId) {
        return tagService.getTagsByUserId(userId);
    }

    @PutMapping("/{id}")
    public TagResponseDto updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDto requestDto
    ) {
        return tagService.updateTag(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}