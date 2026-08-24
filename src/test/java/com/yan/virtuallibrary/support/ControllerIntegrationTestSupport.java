package com.yan.virtuallibrary.support;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.Users.repository.UserBookRepository;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.auth.dto.AuthenticationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ControllerIntegrationTestSupport {

    protected static final String PASSWORD = "123456";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserBookRepository userBookRepository;

    @Autowired
    protected BooksRepository booksRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected void cleanDatabase() {
        userBookRepository.deleteAll();
        booksRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected String createUserAndGenerateToken(String username, String email, Role role) throws Exception {
        UserEntity user = new UserEntity(
                "Test User",
                username,
                email,
                passwordEncoder.encode(PASSWORD),
                role
        );

        userRepository.save(user);

        AuthenticationDTO loginRequest = new AuthenticationDTO(username, PASSWORD);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response)
                .get("token")
                .asText();
    }

    protected BookEntity createBook(String title, String author, String isbn) {
        BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher("Test Publisher");
        book.setIsbn(isbn);
        book.setSynopsis("Test synopsis");
        book.setGenre("Technology");
        book.setCoverUrl("https://example.com/cover.jpg");
        book.setSource(BookSource.MANUAL);
        return booksRepository.save(book);
    }
}
