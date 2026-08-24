package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.mapper.UserBookMapper;
import com.yan.virtuallibrary.Users.repository.UserBookRepository;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.common.exception.BookAlreadyInLibraryException;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import com.yan.virtuallibrary.common.exception.UnauthorizedException;
import com.yan.virtuallibrary.common.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBookServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @Mock
    private UserBookMapper userBookMapper;

    @InjectMocks
    private UserBookService userBookService;

    private UserEntity user;
    private BookEntity book;
    private UserBookEntity userBook;

    @BeforeEach
    void setUp() {

        user = new UserEntity();
        user.setId(1L);
        user.setName("Yan");
        user.setUsername("yan123");
        user.setEmail("yan@example.com");
        user.setPassword("password");

        book = new BookEntity();
        book.setId(10L);
        book.setExternalId("1234567890");
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R Tolkien");
        book.setPublisher("HarperCollins");
        book.setIsbn("9780007525492");
        book.setSynopsis("A fantasy adventure.");
        book.setGenre("Fantasy");
        book.setCoverUrl("https://example.com/hobbit.jpg");
        book.setSource(BookSource.MANUAL);

        userBook = new UserBookEntity();
        userBook.setId(100L);
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setReadFormat(ReadFormat.PDF);
        userBook.setReadStatus(ReadStatus.READING);
        userBook.setStartedAt(LocalDate.of(2026, 7, 1));
        userBook.setFinishedAt(LocalDate.of(2026, 7, 20));

        lenient()
                .when(userBookMapper.userBookEntityToUserBookResponseDTO(any(UserBookEntity.class)))
                .thenAnswer(invocation -> toUserBookResponseDTO(invocation.getArgument(0)));
    }

    private UserBookResponseDTO toUserBookResponseDTO(UserBookEntity userBookEntity) {
        BookEntity bookEntity = userBookEntity.getBook();
        BookResponseDTO bookResponseDTO = new BookResponseDTO(
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

        return new UserBookResponseDTO(
                userBookEntity.getId(),
                bookResponseDTO,
                userBookEntity.getReadStatus(),
                userBookEntity.getReadFormat(),
                userBookEntity.getStartedAt(),
                userBookEntity.getFinishedAt(),
                null,
                null
        );
    }

    @Test
    void addNewBookToUser_shouldAddBook_whenUserAndBookExist() {

        Long userId = 1L;
        Long bookId = 10L;

        UserBookRequestDTO request = new UserBookRequestDTO(
                bookId,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(booksRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(userBookRepository.existsByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(false);

        when(userBookRepository.save(any(UserBookEntity.class)))
                .thenAnswer(invocation -> {

                    UserBookEntity saved =
                            invocation.getArgument(0);

                    saved.setId(100L);

                    return saved;
                });

        UserBookResponseDTO response =
                userBookService.addNewBookToUser(userId, request);

        assertNotNull(response);

        assertEquals(100L, response.id());
        assertEquals(10L, response.book().id());
        assertEquals("The Hobbit", response.book().title());

        verify(userRepository).findById(userId);

        verify(booksRepository).findById(bookId);

        verify(userBookRepository)
                .existsByUser_IdAndBook_Id(userId, bookId);

        verify(userBookRepository)
                .save(any(UserBookEntity.class));
    }

    @Test
    void addNewBookToUser_shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        Long userId = 99L;
        Long bookId = 10L;

        UserBookRequestDTO request = new UserBookRequestDTO(
                bookId,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userBookService.addNewBookToUser(userId, request)
        );

        verify(userRepository).findById(userId);

        verifyNoInteractions(booksRepository);

        verify(userBookRepository, never())
                .save(any());
    }

    @Test
    void addNewBookToUser_shouldThrowBookNotFoundException_whenBookDoesNotExist() {

        Long userId = 1L;
        Long bookId = 99L;

        UserBookRequestDTO request = new UserBookRequestDTO(
                bookId,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(booksRepository.findById(bookId))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> userBookService.addNewBookToUser(userId, request)
        );

        assertEquals(
                "Book not found!",
                exception.getMessage()
        );

        verify(userRepository).findById(userId);
        verify(booksRepository).findById(bookId);

        verify(userBookRepository, never())
                .save(any());
    }

    @Test
    void addNewBookToUser_shouldThrowBookAlreadyInLibraryException_whenBookAlreadyExists() {

        Long userId = 1L;
        Long bookId = 10L;

        UserBookRequestDTO request = new UserBookRequestDTO(
                bookId,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(booksRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(userBookRepository
                .existsByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(true);

        assertThrows(
                BookAlreadyInLibraryException.class,
                () -> userBookService.addNewBookToUser(userId, request)
        );

        verify(userBookRepository, never())
                .save(any());
    }

    @Test
    void findMyBooks_shouldReturnUserBooks_whenBooksExist() {

        Long userId = 1L;

        when(userBookRepository.findAllByUser_Id(userId))
                .thenReturn(List.of(userBook));

        List<UserBookResponseDTO> response =
                userBookService.findMyBooks(userId);

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(100L, response.get(0).id());
        assertEquals(10L, response.get(0).book().id());
        assertEquals("The Hobbit", response.get(0).book().title());

        verify(userBookRepository)
                .findAllByUser_Id(userId);
    }

    @Test
    void findMyBooks_shouldReturnEmptyList_whenUserHasNoBooks() {

        Long userId = 1L;

        when(userBookRepository.findAllByUser_Id(userId))
                .thenReturn(List.of());

        List<UserBookResponseDTO> response =
                userBookService.findMyBooks(userId);

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(userBookRepository)
                .findAllByUser_Id(userId);
    }

    @Test
    void findMyBooks_shouldThrowUnauthorizedException_whenUserIdIsNull() {

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> userBookService.findMyBooks(null)
        );

        assertEquals(
                "User not recognized; please log in.",
                exception.getMessage()
        );

        verifyNoInteractions(userBookRepository);
    }


    @Test
    void updateBookStatus_shouldUpdateFields_whenValuesAreProvided() {

        Long userId = 1L;
        Long bookId = 10L;

        UserBookUpdateDTO request = new UserBookUpdateDTO(
                null,
                null,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 10)
        );

        when(userBookRepository
                .findByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(Optional.of(userBook));

        when(userBookRepository.save(userBook))
                .thenReturn(userBook);

        UserBookResponseDTO response =
                userBookService.updateBookStatus(
                        userId,
                        bookId,
                        request
                );

        assertNotNull(response);

        assertEquals(
                LocalDate.of(2026, 8, 1),
                userBook.getStartedAt()
        );

        assertEquals(
                LocalDate.of(2026, 8, 10),
                userBook.getFinishedAt()
        );

        verify(userBookRepository)
                .findByUser_IdAndBook_Id(userId, bookId);

        verify(userBookRepository)
                .save(userBook);
    }

    @Test
    void updateBookStatus_shouldKeepOldValues_whenFieldsAreNull() {

        Long userId = 1L;
        Long bookId = 10L;

        LocalDate originalStartedAt =
                LocalDate.of(2026, 7, 1);

        LocalDate originalFinishedAt =
                LocalDate.of(2026, 7, 20);

        userBook.setStartedAt(originalStartedAt);
        userBook.setFinishedAt(originalFinishedAt);

        UserBookUpdateDTO request =
                new UserBookUpdateDTO(
                        null,
                        null,
                        null,
                        null
                );

        when(userBookRepository
                .findByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(Optional.of(userBook));

        when(userBookRepository.save(userBook))
                .thenReturn(userBook);

        userBookService.updateBookStatus(
                userId,
                bookId,
                request
        );

        assertEquals(
                originalStartedAt,
                userBook.getStartedAt()
        );

        assertEquals(
                originalFinishedAt,
                userBook.getFinishedAt()
        );

        verify(userBookRepository)
                .save(userBook);
    }

    @Test
    void updateBookStatus_shouldThrowBookNotFoundException_whenBookIsNotInUserLibrary() {

        Long userId = 1L;
        Long bookId = 99L;

        UserBookUpdateDTO request =
                new UserBookUpdateDTO(
                        null,
                        null,
                        null,
                        null
                );

        when(userBookRepository
                .findByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> userBookService.updateBookStatus(
                        userId,
                        bookId,
                        request
                )
        );

        assertEquals(
                "You don't have this book on your library!",
                exception.getMessage()
        );

        verify(userBookRepository, never())
                .save(any());
    }


    @Test
    void deleteBook_shouldDeleteBook_whenBookExistsInUserLibrary() {

        Long userId = 1L;
        Long bookId = 10L;

        when(userBookRepository
                .findByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(Optional.of(userBook));

        userBookService.deleteBook(userId, bookId);

        verify(userBookRepository)
                .findByUser_IdAndBook_Id(userId, bookId);

        verify(userBookRepository)
                .delete(userBook);
    }

    @Test
    void deleteBook_shouldThrowBookNotFoundException_whenBookDoesNotExistInUserLibrary() {

        Long userId = 1L;
        Long bookId = 99L;

        when(userBookRepository
                .findByUser_IdAndBook_Id(userId, bookId))
                .thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> userBookService.deleteBook(
                        userId,
                        bookId
                )
        );

        assertEquals(
                "You don't have this book on your library!",
                exception.getMessage()
        );

        verify(userBookRepository, never())
                .delete(any());
    }
}
