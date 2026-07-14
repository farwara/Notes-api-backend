package com.farwa.notesapi.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.farwa.notesapi.NotesapiApplication;
import com.farwa.notesapi.model.AppUser;
import com.farwa.notesapi.model.Category;
import com.farwa.notesapi.model.Role;
import com.farwa.notesapi.repository.AppUserRepository;
import com.farwa.notesapi.repository.CategoryRepository;
import com.farwa.notesapi.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NotesapiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();

        String uniqueEmail =
                "note-test-" + System.nanoTime() + "@test.com";

        testUser = AppUser.builder()
                .name("Test User")
                .email(uniqueEmail)
                .role(Role.USER)
                .build();

        testUser = appUserRepository.save(testUser);

        testCategory = Category.builder()
                .name("Work " + System.nanoTime())
                .description("Work-related notes")
                .color("Blue")
                .createdBy(testUser)
                .build();

        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void createNote_shouldReturnCreatedNote() throws Exception {
        String requestBody = """
                {
                  "title": "Finish backend assignment",
                  "content": "Complete integration tests",
                  "userId": %d,
                  "categoryId": %d,
                  "tagIds": []
                }
                """.formatted(
                testUser.getId(),
                testCategory.getId()
        );

        mockMvc.perform(
                        post("/api/notes")
                                .with(authenticatedUser())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title")
                        .value("Finish backend assignment"))
                .andExpect(jsonPath("$.content")
                        .value("Complete integration tests"));
    }

    @Test
    void getNoteById_shouldReturnExistingNote() throws Exception {
        Long noteId = createTestNoteAndReturnId();

        mockMvc.perform(
                        get("/api/notes/{id}", noteId)
                                .with(authenticatedUser())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteId))
                .andExpect(jsonPath("$.title")
                        .value("Original note"))
                .andExpect(jsonPath("$.content")
                        .value("Original content"));
    }

    @Test
    void getAllNotes_shouldReturnSavedNotes() throws Exception {
        createTestNoteAndReturnId();

        mockMvc.perform(
                        get("/api/notes")
                                .with(authenticatedUser())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title")
                        .value("Original note"));
    }

    @Test
    void updateNote_shouldReturnUpdatedNote() throws Exception {
        Long noteId = createTestNoteAndReturnId();

        String updateBody = """
                {
                  "title": "Updated note",
                  "content": "Updated integration-test content",
                  "userId": %d,
                  "categoryId": %d,
                  "tagIds": []
                }
                """.formatted(
                testUser.getId(),
                testCategory.getId()
        );

        mockMvc.perform(
                        put("/api/notes/{id}", noteId)
                                .with(authenticatedUser())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteId))
                .andExpect(jsonPath("$.title")
                        .value("Updated note"))
                .andExpect(jsonPath("$.content")
                        .value("Updated integration-test content"));
    }

    @Test
    void getNoteById_whenNoteDoesNotExist_shouldReturnNotFound()
            throws Exception {

        mockMvc.perform(
                        get("/api/notes/99999")
                                .with(authenticatedUser())
                )
                .andExpect(status().isNotFound());
    }

    private Long createTestNoteAndReturnId() throws Exception {
        String requestBody = """
                {
                  "title": "Original note",
                  "content": "Original content",
                  "userId": %d,
                  "categoryId": %d,
                  "tagIds": []
                }
                """.formatted(
                testUser.getId(),
                testCategory.getId()
        );

        MvcResult result = mockMvc.perform(
                        post("/api/notes")
                                .with(authenticatedUser())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode responseJson = objectMapper.readTree(
                result.getResponse().getContentAsString()
        );

        return responseJson.get("id").asLong();
    }

    private RequestPostProcessor authenticatedUser() {
        return jwt().authorities(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }
}

