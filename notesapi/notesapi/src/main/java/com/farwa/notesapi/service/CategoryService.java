package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.CategoryRequestDto;
import com.farwa.notesapi.dto.CategoryResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Category;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        AppUser admin = appUserRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        Category category = Category.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .color(requestDto.getColor())
                .createdBy(admin)
                .build();

        return mapToResponseDto(categoryRepository.save(category));
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return mapToResponseDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        category.setColor(requestDto.getColor());

        return mapToResponseDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }

        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto mapToResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .createdByAdminId(
                        category.getCreatedBy() != null ? category.getCreatedBy().getId() : null
                )
                .build();
    }
}