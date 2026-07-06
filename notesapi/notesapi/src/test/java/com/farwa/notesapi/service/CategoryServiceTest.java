package com.farwa.notesapi.service;

import com.farwa.notesapi.dto.CategoryRequestDto;
import com.farwa.notesapi.dto.CategoryResponseDto;
import com.farwa.notesapi.exception.ResourceNotFoundException;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Category;
import com.farwa.notesapi.model.Role;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        AppUser admin = AppUser.builder()
                .id(1L)
                .name("Admin")
                .email("admin@test.com")
                .role(Role.ADMIN)
                .build();

        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("School");
        request.setDescription("School notes");
        request.setColor("blue");
        request.setAdminId(1L);

        Category savedCategory = Category.builder()
                .id(1L)
                .name("School")
                .description("School notes")
                .color("blue")
                .createdBy(admin)
                .build();

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponseDto response = categoryService.createCategory(request);

        assertEquals("School", response.getName());
        assertEquals("School notes", response.getDescription());
        assertEquals("blue", response.getColor());
        assertEquals(1L, response.getCreatedByAdminId());

        verify(appUserRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategories_shouldReturnCategoryList() {
        Category category = Category.builder()
                .id(1L)
                .name("Work")
                .description("Work notes")
                .color("green")
                .build();

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponseDto> response = categoryService.getAllCategories();

        assertEquals(1, response.size());
        assertEquals("Work", response.get(0).getName());

        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_shouldReturnCategory_whenCategoryExists() {
        Category category = Category.builder()
                .id(1L)
                .name("Personal")
                .description("Personal notes")
                .color("yellow")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDto response = categoryService.getCategoryById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Personal", response.getName());

        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_shouldThrowException_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));

        verify(categoryRepository).findById(99L);
    }

    @Test
    void deleteCategory_shouldDeleteCategory_whenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }
}