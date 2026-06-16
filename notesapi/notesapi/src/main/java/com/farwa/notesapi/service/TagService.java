package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.TagRequestDto;
import com.farwa.notesapi.dto.TagResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Tag;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final AppUserRepository appUserRepository;

    public TagResponseDto createTag(TagRequestDto requestDto) {
        AppUser user = appUserRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tag tag = Tag.builder()
                .name(requestDto.getName())
                .color(requestDto.getColor())
                .user(user)
                .build();

        return mapToResponseDto(tagRepository.save(tag));
    }

    public List<TagResponseDto> getTagsByUserId(Long userId) {
        return tagRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public TagResponseDto updateTag(Long id, TagRequestDto requestDto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        tag.setName(requestDto.getName());
        tag.setColor(requestDto.getColor());

        return mapToResponseDto(tagRepository.save(tag));
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found");
        }

        tagRepository.deleteById(id);
    }

    private TagResponseDto mapToResponseDto(Tag tag) {
        return TagResponseDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .userId(tag.getUser().getId())
                .build();
    }
}