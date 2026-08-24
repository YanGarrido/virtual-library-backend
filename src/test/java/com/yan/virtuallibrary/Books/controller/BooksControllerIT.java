package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.client.GoogleBooksClient;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksImageLinksDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksIndustryIdentifierDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeInfoDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeListDTO;
import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.ImportBookDTO;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.support.ControllerIntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BooksControllerIT extends ControllerIntegrationTestSupport {

    @MockitoBean
    private GoogleBooksClient googleBooksClient;

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Test
    void findAllBooks_shouldReturnForbidden_whenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAllBooks_shouldReturnOk_whenTokenIsValid() throws Exception {
        createBook("Clean Code", "Robert C. Martin", "9780132350884");
        String token = createUserAndGenerateToken("reader", "reader@example.com", Role.USER);

        mockMvc.perform(get("/books")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void findBook_shouldReturnBook_whenBookExistsAndTokenIsValid() throws Exception {
        BookEntity book = createBook("Effective Java", "Joshua Bloch", "9780134685991");
        String token = createUserAndGenerateToken("findbook", "findbook@example.com", Role.USER);

        mockMvc.perform(get("/books/" + book.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void findBooks_shouldReturnMatches_whenFiltersAreProvided() throws Exception {
        createBook("Domain-Driven Design", "Eric Evans", "9780321125217");
        String token = createUserAndGenerateToken("searchuser", "searchuser@example.com", Role.USER);

        mockMvc.perform(get("/books/search")
                        .header("Authorization", "Bearer " + token)
                        .param("title", "domain")
                        .param("author", "eric"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Domain-Driven Design"));
    }

    @Test
    void createBook_shouldReturnForbidden_whenTokenIsMissing() throws Exception {
        BookRequestDTO request = bookRequest("Clean Code");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBook_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        String userToken = createUserAndGenerateToken("commonuser", "common@example.com", Role.USER);
        BookRequestDTO request = bookRequest("Clean Code");

        mockMvc.perform(post("/books")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBook_shouldReturnCreated_whenUserIsAdmin() throws Exception {
        String adminToken = createUserAndGenerateToken("adminuser", "admin@example.com", Role.ADMIN);
        BookRequestDTO request = bookRequest("Clean Code");

        mockMvc.perform(post("/books")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));
    }

    @Test
    void updateBook_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        BookEntity book = createBook("Old Title", "Old Author", "1234567890");
        String userToken = createUserAndGenerateToken("updateuser", "updateuser@example.com", Role.USER);
        BookRequestDTO request = bookRequest("New Title");

        mockMvc.perform(put("/books/" + book.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateBook_shouldReturnOk_whenUserIsAdmin() throws Exception {
        BookEntity book = createBook("Old Title", "Old Author", "1234567890");
        String adminToken = createUserAndGenerateToken("updateadmin", "updateadmin@example.com", Role.ADMIN);
        BookRequestDTO request = bookRequest("New Title");

        mockMvc.perform(put("/books/" + book.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void deleteBook_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        BookEntity book = createBook("Clean Code", "Robert C. Martin", "9780132350884");
        String userToken = createUserAndGenerateToken("deleteuser", "deleteuser@example.com", Role.USER);

        mockMvc.perform(delete("/books/" + book.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBook_shouldReturnNoContent_whenUserIsAdmin() throws Exception {
        BookEntity book = createBook("Clean Code", "Robert C. Martin", "9780132350884");
        String adminToken = createUserAndGenerateToken("deleteadmin", "deleteadmin@example.com", Role.ADMIN);

        mockMvc.perform(delete("/books/" + book.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchExternalBooks_shouldReturnGoogleBooksResults_whenTokenIsValid() throws Exception {
        String token = createUserAndGenerateToken("externaluser", "externaluser@example.com", Role.USER);
        when(googleBooksClient.searchBooks("clean code", 10))
                .thenReturn(new GoogleBooksVolumeListDTO(1, List.of(googleVolume("google-1"))));

        mockMvc.perform(get("/books/external/search")
                        .header("Authorization", "Bearer " + token)
                        .param("query", "clean code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].externalId").value("google-1"))
                .andExpect(jsonPath("$[0].title").value("Imported Book"));
    }

    @Test
    void importExternalBook_shouldReturnCreated_whenUserIsAdmin() throws Exception {
        String adminToken = createUserAndGenerateToken("importadmin", "importadmin@example.com", Role.ADMIN);
        when(googleBooksClient.findBookById("google-1"))
                .thenReturn(googleVolume("google-1"));

        ImportBookDTO request = new ImportBookDTO("google-1");

        mockMvc.perform(post("/books/import")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.externalId").value("google-1"))
                .andExpect(jsonPath("$.title").value("Imported Book"));
    }

    private BookRequestDTO bookRequest(String title) {
        return new BookRequestDTO(
                null,
                title,
                "Robert C. Martin",
                "Prentice Hall",
                "9780132350884",
                "Software craftsmanship book.",
                "Software Engineering",
                "https://example.com/clean-code.jpg"
        );
    }

    private GoogleBooksVolumeDTO googleVolume(String id) {
        GoogleBooksVolumeInfoDTO volumeInfo = new GoogleBooksVolumeInfoDTO(
                "Imported Book",
                List.of("External Author"),
                "External Publisher",
                "External synopsis",
                List.of("Technology"),
                List.of(new GoogleBooksIndustryIdentifierDTO("ISBN_13", "9780132350884")),
                new GoogleBooksImageLinksDTO("https://example.com/small.jpg", "https://example.com/thumb.jpg")
        );

        return new GoogleBooksVolumeDTO(id, volumeInfo);
    }
}
