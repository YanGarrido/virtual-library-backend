package com.yan.virtuallibrary.Books.service;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.dto.BookSearchResponseDTO;
import com.yan.virtuallibrary.Books.mapper.BookMapper;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class BookServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private BookEntity book;

    @BeforeEach
    void setUp() {
        book = new BookEntity();

        book.setId(1L);
        book.setExternalId("EXT-001");
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setPublisher("HarperCollins");
        book.setIsbn("9780007525492");
        book.setSynopsis("Bilbo Baggins goes on an adventure.");
        book.setGenre("Fantasy");
        book.setCoverUrl("https://example.com/hobbit.jpg");

        // Troque pelo enum correto do seu projeto
        book.setSource(BookSource.MANUAL);

        lenient()
                .when(bookMapper.bookEntityToBookResponseDTO(any(BookEntity.class)))
                .thenAnswer(invocation -> toBookResponseDTO(invocation.getArgument(0)));
    }

    private BookResponseDTO toBookResponseDTO(BookEntity bookEntity) {
        return new BookResponseDTO(
                bookEntity.getId(),
                bookEntity.getExternalId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getIsbn(),
                bookEntity.getSynopsis(),
                bookEntity.getGenre(),
                bookEntity.getCoverUrl(),
                bookEntity.getSource().name()
        );
    }

    @Test
    void createBook_shouldCreateBook_whenRequestIsValid() {

        BookRequestDTO request = new BookRequestDTO(
                "EXT-001",
                "The Hobbit",
                "J.R.R. Tolkien",
                "HarperCollins",
                "9780007525492",
                "Bilbo Baggins goes on an adventure.",
                "Fantasy",
                "https://example.com/hobbit.jpg"
        );

        when(booksRepository.save(any(BookEntity.class)))
                .thenAnswer(invocation -> {
                    BookEntity savedBook = invocation.getArgument(0);
                    savedBook.setId(1L);
                    savedBook.setSource(BookSource.MANUAL);
                    return savedBook;
                });

        BookResponseDTO response = bookService.createBook(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("EXT-001", response.externalId());
        assertEquals("The Hobbit", response.title());
        assertEquals("J.R.R. Tolkien", response.author());
        assertEquals("HarperCollins", response.publisher());
        assertEquals("9780007525492", response.isbn());
        assertEquals("Bilbo Baggins goes on an adventure.", response.synopsis());
        assertEquals("Fantasy", response.genre());
        assertEquals("https://example.com/hobbit.jpg", response.coverUrl());
        assertEquals("MANUAL", response.source());

        verify(booksRepository).save(any(BookEntity.class));
    }

    @Test
    void findAllBooks_shouldReturnAllBooks_whenBooksExist() {

        BookEntity secondBook = new BookEntity();

        secondBook.setId(2L);
        secondBook.setExternalId("EXT-002");
        secondBook.setTitle("1984");
        secondBook.setAuthor("George Orwell");
        secondBook.setPublisher("Penguin");
        secondBook.setIsbn("9780451524935");
        secondBook.setSynopsis("A dystopian novel.");
        secondBook.setGenre("Dystopia");
        secondBook.setCoverUrl("https://example.com/1984.jpg");
        secondBook.setSource(BookSource.MANUAL);

        when(booksRepository.findAll())
                .thenReturn(List.of(book, secondBook));

        List<BookResponseDTO> response = bookService.findAllBooks();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("The Hobbit", response.get(0).title());
        assertEquals("1984", response.get(1).title());

        verify(booksRepository).findAll();
    }

    @Test
    void findAllBooks_shouldReturnEmptyList_whenNoBooksExist() {

        when(booksRepository.findAll())
                .thenReturn(List.of());

        List<BookResponseDTO> response = bookService.findAllBooks();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(booksRepository).findAll();
    }

    @Test
    void findBook_shouldReturnBook_whenBookExists() {

        Long id = 1L;

        when(booksRepository.findById(id))
                .thenReturn(Optional.of(book));

        BookResponseDTO response = bookService.findBook(id);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("The Hobbit", response.title());
        assertEquals("J.R.R. Tolkien", response.author());
        assertEquals("Fantasy", response.genre());

        verify(booksRepository).findById(id);
    }

    @Test
    void findBook_shouldThrowBookNotFoundException_whenBookDoesNotExist() {

        Long id = 99L;

        when(booksRepository.findById(id))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.findBook(id)
        );

        assertEquals("Book not found!", exception.getMessage());

        verify(booksRepository).findById(id);
    }

    @Test
    void updateBooks_shouldUpdateBook_whenBookExists() {

        Long id = 1L;

        BookRequestDTO request = new BookRequestDTO(
                "EXT-001",
                "The Hobbit Updated",
                "J.R.R. Tolkien",
                "New Publisher",
                "9780007525492",
                "Updated synopsis",
                "Adventure",
                "https://example.com/new-cover.jpg"
        );

        when(booksRepository.findById(id))
                .thenReturn(Optional.of(book));

        when(booksRepository.save(book))
                .thenReturn(book);

        BookResponseDTO response = bookService.updateBooks(id, request);

        assertNotNull(response);

        assertEquals("The Hobbit Updated", response.title());
        assertEquals("J.R.R. Tolkien", response.author());
        assertEquals("New Publisher", response.publisher());
        assertEquals("9780007525492", response.isbn());
        assertEquals("Updated synopsis", response.synopsis());
        assertEquals("Adventure", response.genre());
        assertEquals("https://example.com/new-cover.jpg", response.coverUrl());

        verify(booksRepository).findById(id);
        verify(booksRepository).save(book);
    }

    @Test
    void updateBooks_shouldThrowBookNotFoundException_whenBookDoesNotExist() {

        Long id = 99L;

        BookRequestDTO request = new BookRequestDTO(
                "EXT-001",
                "The Hobbit Updated",
                "J.R.R. Tolkien",
                "New Publisher",
                "9780007525492",
                "Updated synopsis",
                "Adventure",
                "https://example.com/new-cover.jpg"
        );

        when(booksRepository.findById(id))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.updateBooks(id, request)
        );

        assertEquals("Book not found", exception.getMessage());

        verify(booksRepository).findById(id);
        verify(booksRepository, never()).save(any());
    }

    @Test
    void deleteBook_shouldDeleteBook_whenBookExists() {

        Long id = 1L;

        when(booksRepository.findById(id))
                .thenReturn(Optional.of(book));

        bookService.deleteBook(id);

        verify(booksRepository).findById(id);
        verify(booksRepository).delete(book);
    }

    @Test
    void deleteBook_shouldThrowBookNotFoundException_whenBookDoesNotExist() {

        Long id = 99L;

        when(booksRepository.findById(id))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(id)
        );

        assertEquals("Book not found", exception.getMessage());

        verify(booksRepository).findById(id);
        verify(booksRepository, never()).delete(any());
    }

    @Test
    void findBooks_shouldReturnBooks_whenFiltersMatch() {

        when(booksRepository
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                        "Hobbit",
                        "Tolkien",
                        "Fantasy",
                        "978"
                ))
                .thenReturn(List.of(book));

        List<BookSearchResponseDTO> response =
                bookService.findBooks(
                        "Hobbit",
                        "Tolkien",
                        "Fantasy",
                        "978"
                );

        assertNotNull(response);
        assertEquals(1, response.size());

        BookSearchResponseDTO result = response.get(0);

        assertEquals("EXT-001", result.externalId());
        assertEquals("The Hobbit", result.title());
        assertEquals("J.R.R. Tolkien", result.author());
        assertEquals("Fantasy", result.genre());

        verify(booksRepository)
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                        "Hobbit",
                        "Tolkien",
                        "Fantasy",
                        "978"
                );
    }

    @Test
    void findBooks_shouldUseEmptyStrings_whenFiltersAreNull() {

        when(booksRepository
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                        "",
                        "",
                        "",
                        ""
                ))
                .thenReturn(List.of(book));

        List<BookSearchResponseDTO> response =
                bookService.findBooks(null, null, null, null);

        assertEquals(1, response.size());
        assertEquals("The Hobbit", response.get(0).title());

        verify(booksRepository)
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                        "",
                        "",
                        "",
                        ""
                );
    }

    @Test
    void findBooks_shouldReturnEmptyList_whenNoBooksMatch() {

        when(booksRepository
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                        "Nonexistent",
                        "",
                        "",
                        ""
                ))
                .thenReturn(List.of());

        List<BookSearchResponseDTO> response =
                bookService.findBooks(
                        "Nonexistent",
                        null,
                        null,
                        null
                );

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
