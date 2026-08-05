package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.repository.UserBookRepository;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserBookService {
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final UserBookRepository userBookRepository;

    public UserBookService(UserRepository userRepository, BooksRepository booksRepository, UserBookRepository userBookRepository) {
        this.userRepository = userRepository;
        this.booksRepository = booksRepository;
        this.userBookRepository = userBookRepository;
    }

    public UserBookResponseDTO addNewBookToUser(Long id, UserBookRequestDTO userBookRequestDTO){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        BooksEntity book = booksRepository.findById(userBookRequestDTO.bookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));


        UserBookEntity userBookEntity = new UserBookEntity();
        userBookEntity.setReadStatus(userBookRequestDTO.readStatus());
        userBookEntity.setReadFormat(userBookRequestDTO.readFormat());
        userBookEntity.setStartedAt(userBookRequestDTO.startedAt());
        userBookEntity.setFinishedAt(userBookRequestDTO.finishedAt());
        userBookEntity.setUser(user);
        userBookEntity.setBook(book);

        UserBookEntity savedUserBook = userBookRepository.save(userBookEntity);

        return new UserBookResponseDTO(
                savedUserBook.getId(),
                new BookResponseDTO(
                        savedUserBook.getBook().getId(),
                        savedUserBook.getBook().getExternalId(),
                        savedUserBook.getBook().getTitle(),
                        savedUserBook.getBook().getAuthor(),
                        savedUserBook.getBook().getPublisher(),
                        savedUserBook.getBook().getIsbn(),
                        savedUserBook.getBook().getSynopsis(),
                        savedUserBook.getBook().getGenre(),
                        savedUserBook.getBook().getCoverUrl(),
                        savedUserBook.getBook().getSource().name()
                ),
                savedUserBook.getReadStatus(),
                savedUserBook.getReadFormat(),
                savedUserBook.getStartedAt(),
                savedUserBook.getFinishedAt(),
                savedUserBook.getCreated_at(),
                savedUserBook.getUpdated_at()
        );

    }

}

