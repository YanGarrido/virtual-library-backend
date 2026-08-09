package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.repository.UserBookRepository;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.common.exception.BookAlreadyInLibraryException;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import com.yan.virtuallibrary.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(UserNotFoundException::new);

        BookEntity book = booksRepository.findById(userBookRequestDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found!"));

        if(userBookRepository.existsByUser_IdAndBook_Id(id, userBookRequestDTO.bookId())){
            throw new BookAlreadyInLibraryException();
        }

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
    
    public List<UserBookResponseDTO> findMyBooks(Long id){
        if(id == null){
            throw new BookNotFoundException("Book not found!");
        }
        return userBookRepository.findAllByUser_Id(id).stream()
                .map(userBookEntity -> new UserBookResponseDTO(
                        userBookEntity.getId(),
                        new BookResponseDTO(
                                userBookEntity.getBook().getId(),
                                userBookEntity.getBook().getExternalId(),
                                userBookEntity.getBook().getTitle(),
                                userBookEntity.getBook().getAuthor(),
                                userBookEntity.getBook().getPublisher(),
                                userBookEntity.getBook().getIsbn(),
                                userBookEntity.getBook().getSynopsis(),
                                userBookEntity.getBook().getGenre(),
                                userBookEntity.getBook().getCoverUrl(),
                                userBookEntity.getBook().getSource().name()
                        ),
                        userBookEntity.getReadStatus(),
                        userBookEntity.getReadFormat(),
                        userBookEntity.getStartedAt(),
                        userBookEntity.getFinishedAt(),
                        userBookEntity.getCreated_at(),
                        userBookEntity.getUpdated_at()
                ))
                .toList();
    }

    public UserBookResponseDTO updateBookStatus(Long userId, Long bookId, UserBookUpdateDTO userBookUpdateDTO){
        UserBookEntity userBook = userBookRepository.findByUser_IdAndBook_Id(userId, bookId)
                .orElseThrow(() -> new BookNotFoundException("You don't have this book on your library!"));

        if(userBookUpdateDTO.readStatus() != null){ userBook.setReadStatus(userBookUpdateDTO.readStatus());}
        if(userBookUpdateDTO.readFormat() != null){ userBook.setReadFormat(userBookUpdateDTO.readFormat());}
        if(userBookUpdateDTO.startedAt() != null){ userBook.setStartedAt(userBookUpdateDTO.startedAt());}
        if(userBookUpdateDTO.finishedAt() != null){ userBook.setFinishedAt(userBookUpdateDTO.finishedAt());}

        UserBookEntity updatedUserBook = userBookRepository.save(userBook);

        return new UserBookResponseDTO(
                updatedUserBook.getId(),
                new BookResponseDTO(
                        updatedUserBook.getBook().getId(),
                        updatedUserBook.getBook().getExternalId(),
                        updatedUserBook.getBook().getTitle(),
                        updatedUserBook.getBook().getAuthor(),
                        updatedUserBook.getBook().getPublisher(),
                        updatedUserBook.getBook().getIsbn(),
                        updatedUserBook.getBook().getSynopsis(),
                        updatedUserBook.getBook().getGenre(),
                        updatedUserBook.getBook().getCoverUrl(),
                        updatedUserBook.getBook().getSource().name()
                ),
                updatedUserBook.getReadStatus(),
                updatedUserBook.getReadFormat(),
                updatedUserBook.getStartedAt(),
                updatedUserBook.getFinishedAt(),
                updatedUserBook.getCreated_at(),
                updatedUserBook.getUpdated_at()
        );
    }

}

