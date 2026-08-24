package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.support.ControllerIntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserBookControllerIT extends ControllerIntegrationTestSupport {

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Test
    void addNewBookToUser_shouldReturnForbidden_whenTokenIsMissing() throws Exception {
        UserBookRequestDTO request = new UserBookRequestDTO(
                1L,
                ReadStatus.READING,
                ReadFormat.PDF,
                LocalDate.of(2026, 8, 1),
                null
        );

        mockMvc.perform(post("/users/me/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addNewBookToUser_shouldReturnCreated_whenBookExists() throws Exception {
        BookEntity book = createBook("Clean Code", "Robert C. Martin", "9780132350884");
        String token = createUserAndGenerateToken("libraryuser", "libraryuser@example.com", Role.USER);
        UserBookRequestDTO request = userBookRequest(book.getId());

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.title").value("Clean Code"))
                .andExpect(jsonPath("$.readStatus").value("READING"))
                .andExpect(jsonPath("$.readFormat").value("PDF"));
    }

    @Test
    void addNewBookToUser_shouldReturnNotFound_whenBookDoesNotExist() throws Exception {
        String token = createUserAndGenerateToken("missingbookuser", "missingbookuser@example.com", Role.USER);
        UserBookRequestDTO request = userBookRequest(999L);

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found!"));
    }

    @Test
    void addNewBookToUser_shouldReturnConflict_whenBookAlreadyExistsInLibrary() throws Exception {
        BookEntity book = createBook("Clean Code", "Robert C. Martin", "9780132350884");
        String token = createUserAndGenerateToken("duplicatebookuser", "duplicatebookuser@example.com", Role.USER);
        UserBookRequestDTO request = userBookRequest(book.getId());

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void findMyBooks_shouldReturnBooks_whenUserHasBooks() throws Exception {
        BookEntity book = createBook("Effective Java", "Joshua Bloch", "9780134685991");
        String token = createUserAndGenerateToken("listbooksuser", "listbooksuser@example.com", Role.USER);

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBookRequest(book.getId()))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/me/books")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].book.title").value("Effective Java"));
    }

    @Test
    void findMyBooks_shouldReturnEmptyList_whenUserHasNoBooks() throws Exception {
        String token = createUserAndGenerateToken("emptylibraryuser", "emptylibraryuser@example.com", Role.USER);

        mockMvc.perform(get("/users/me/books")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void updateBookStatus_shouldReturnUpdatedBook_whenBookExistsInUserLibrary() throws Exception {
        BookEntity book = createBook("Refactoring", "Martin Fowler", "9780134757599");
        String token = createUserAndGenerateToken("updatelibraryuser", "updatelibraryuser@example.com", Role.USER);

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBookRequest(book.getId()))))
                .andExpect(status().isCreated());

        UserBookUpdateDTO request = new UserBookUpdateDTO(
                ReadStatus.FINISHED,
                ReadFormat.KINDLE,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 20)
        );

        mockMvc.perform(patch("/users/me/books/" + book.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.readStatus").value("FINISHED"))
                .andExpect(jsonPath("$.readFormat").value("KINDLE"))
                .andExpect(jsonPath("$.finishedAt").value("2026-08-20"));
    }

    @Test
    void deleteBook_shouldReturnNoContent_whenBookExistsInUserLibrary() throws Exception {
        BookEntity book = createBook("Refactoring", "Martin Fowler", "9780134757599");
        String token = createUserAndGenerateToken("deletelibraryuser", "deletelibraryuser@example.com", Role.USER);

        mockMvc.perform(post("/users/me/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBookRequest(book.getId()))))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/users/me/books/" + book.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private UserBookRequestDTO userBookRequest(Long bookId) {
        return new UserBookRequestDTO(
                bookId,
                ReadStatus.READING,
                ReadFormat.PDF,
                LocalDate.of(2026, 8, 1),
                null
        );
    }
}
