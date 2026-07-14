
package com.farwa.notesapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farwa.notesapi.dto.CategoryRequestDto;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Role;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private AppUser admin;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        appUserRepository.deleteAll();

        admin = AppUser.builder()
                .name("Test Admin")
                .email("integration-admin@test.com")
                .role(Role.ADMIN)
                .build();

        admin = appUserRepository.save(admin);
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() throws Exception {
        // Arrange
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Work");
        request.setDescription("Work-related notes");
        request.setColor("Blue");
        request.setAdminId(admin.getId());

        // Act and assert
        mockMvc.perform(
                        post("/api/categories")
                                .with(jwt().authorities(
                                        new SimpleGrantedAuthority("ROLE_ADMIN")
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Work"))
                .andExpect(jsonPath("$.description")
                        .value("Work-related notes"))
                .andExpect(jsonPath("$.color").value("Blue"))
                .andExpect(jsonPath("$.createdByAdminId")
                        .value(admin.getId()));
    }

    @Test
    void getAllCategories_shouldReturnSavedCategories() throws Exception {
        // Arrange: create a category using the real endpoint
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Personal");
        request.setDescription("Personal notes");
        request.setColor("Green");
        request.setAdminId(admin.getId());

        mockMvc.perform(
                        post("/api/categories")
                                .with(jwt().authorities(
                                        new SimpleGrantedAuthority("ROLE_ADMIN")
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        // Act and assert
        mockMvc.perform(
                        get("/api/categories")
                                .with(jwt().authorities(
                                        new SimpleGrantedAuthority("ROLE_ADMIN")
                                ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Personal"))
                .andExpect(jsonPath("$[0].description")
                        .value("Personal notes"))
                .andExpect(jsonPath("$[0].color")
                        .value("Green"))
                .andExpect(jsonPath("$[0].createdByAdminId")
                        .value(admin.getId()));
    }

    @Test
    void createCategory_withoutName_shouldReturnBadRequest()
            throws Exception {

        // Arrange
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("");
        request.setDescription("Invalid category");
        request.setColor("Red");
        request.setAdminId(admin.getId());

        // Act and assert
        mockMvc.perform(
                        post("/api/categories")
                                .with(jwt().authorities(
                                        new SimpleGrantedAuthority("ROLE_ADMIN")
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCategoryById_whenCategoryDoesNotExist_shouldReturnNotFound()
            throws Exception {

        mockMvc.perform(
                        get("/api/categories/99999")
                                .with(jwt().authorities(
                                        new SimpleGrantedAuthority("ROLE_ADMIN")
                                ))
                )
                .andExpect(status().isNotFound());
    }
}